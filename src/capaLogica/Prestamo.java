package capaLogica;

import capaDatos.clsJDBC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;

/**
 *
 * @author FABIAN VERA
 */
public class Prestamo {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;
    Connection con = null;
    Statement sent;

    public Integer generarCodigoPrestamo() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idprestamo),0)+1 AS codigo FROM prestamo;";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de préstamo: " + e.getMessage());
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception ex) {
            }
        }

        return 0;
    }

    public Integer generarCodigoDetalle() throws Exception {
        strSQL = "SELECT COALESCE(MAX(iddetalle),0)+1 AS codigo FROM detalle_prestamo;";
        ResultSet rs = null;

        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar iddetalle: " + e.getMessage());
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception ex) {
            }
        }

        return 0;
    }

    public boolean tienePrestamosActivos(int idLector) throws Exception {
        try {
            con = objConectar.conectar();
            sent = con.createStatement();

            strSQL = "SELECT 1 FROM prestamo "
                    + "WHERE idlector = " + idLector + " AND estado = true "
                    + "LIMIT 1";

            rs = sent.executeQuery(strSQL);

            return rs.next();

        } catch (Exception e) {
            throw new Exception("Error al verificar préstamos activos: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }

    public boolean tieneMultasPendientes(int idLector) throws Exception {
        try {
            con = objConectar.conectar();
            sent = con.createStatement();

            strSQL = "SELECT 1 FROM multa "
                    + "WHERE idlector = " + idLector + " AND pagado = false "
                    + "LIMIT 1";

            rs = sent.executeQuery(strSQL);

            return rs.next();  // true si tiene multas pendientes

        } catch (Exception e) {
            throw new Exception("Error al verificar multas pendientes: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }

    public void registrarPrestamo(Integer idPrestamo,
            Integer idLector,
            Integer idBibliotecario,
            String fechaEstimado,
            JTable tabla) throws Exception {

        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);
            sent = con.createStatement();

            // ===================================
            // 1. INSERTAR PRESTAMO
            // ===================================
            strSQL = "INSERT INTO prestamo (idprestamo, idusuariolector, idusuariobibliotecario, fechaprestamo, fechadevolucionestimada, estado) "
                    + "VALUES (" + idPrestamo + ", " + idLector + ", " + idBibliotecario
                    + ", CURRENT_DATE, '" + fechaEstimado + "', 'activo')";

            sent.executeUpdate(strSQL);

            // Validación: No permitir préstamo sin ejemplares
            int filas = tabla.getRowCount();
            if (filas == 0) {
                throw new Exception("Debe agregar al menos un ejemplar al préstamo.");
            }

            // ===================================
            // 2. INSERTAR DETALLE_PRESTAMO
            // ===================================
            for (int i = 0; i < filas; i++) {

                Integer iddetalle = generarCodigoDetalle();
                String idejemplar = tabla.getValueAt(i, 0).toString();

                // Observación segura
                String obs = "";
                if (tabla.getValueAt(i, 3) != null) {
                    obs = tabla.getValueAt(i, 3).toString().trim().replace("'", "''");
                }

                strSQL = "INSERT INTO detalle_prestamo "
                        + "(iddetalle, idprestamo, idejemplar, observaciones, estado) VALUES ("
                        + iddetalle + ", " + idPrestamo + ", " + idejemplar + ", '"
                        + obs + "', TRUE)";

                sent.executeUpdate(strSQL);

                // ===================================
                // 3. ACTUALIZAR EJEMPLAR (Prestado)
                // ===================================
                strSQL = "UPDATE ejemplar SET estado = FALSE, estado_devolucion = FALSE "
                        + "WHERE idejemplar = " + idejemplar;

                sent.executeUpdate(strSQL);
            }

            // Confirmar transacción
            con.commit();

        } catch (Exception e) {

            // Rollback seguro
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Error durante rollback: " + ex.getMessage());
            }

            throw new Exception("Error en transacción de préstamo: " + e.getMessage());

        } finally {

            try {
                if (sent != null) {
                    sent.close();
                }
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (Exception ex) {
            }
        }
    }

    public ResultSet listarPrestamosDeUsuario(int idUsuario) throws Exception {
        try {
            strSQL = "SELECT p.idprestamo, p.fechaprestamo, p.fechadevolucionestimada, p.estado, "
                    + "STRING_AGG(l.titulo, ', ') AS libros "
                    + "FROM prestamo p "
                    + "INNER JOIN detalle_prestamo dp ON p.idprestamo = dp.idprestamo "
                    + "INNER JOIN ejemplar e ON dp.idejemplar = e.idejemplar "
                    + "INNER JOIN libros l ON e.idlibro = l.idlibro "
                    + "WHERE p.idusuariolector = " + idUsuario + " "
                    + "GROUP BY p.idprestamo, p.fechaprestamo, p.fechadevolucionestimada, p.estado "
                    + "ORDER BY p.idprestamo DESC";

            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar préstamos: " + e.getMessage());
        }
    }

    // ===============================================================
    // 2. LISTAR PRÉSTAMOS DEL USUARIO FILTRADOS POR FECHA
    // ===============================================================
    public ResultSet listarPrestamosPorFecha(int idUsuario, String fecha) throws Exception {
        try {
            strSQL = "SELECT p.idprestamo, p.fechaprestamo, p.fechadevolucionestimada, p.estado, "
                    + "STRING_AGG(l.titulo, ', ') AS libros "
                    + "FROM prestamo p "
                    + "INNER JOIN detalle_prestamo dp ON p.idprestamo = dp.idprestamo "
                    + "INNER JOIN ejemplar e ON dp.idejemplar = e.idejemplar "
                    + "INNER JOIN libros l ON e.idlibro = l.idlibro "
                    + "WHERE p.idusuariolector = " + idUsuario + " "
                    + "AND p.fechaprestamo = '" + fecha + "' "
                    + "GROUP BY p.idprestamo, p.fechaprestamo, p.fechadevolucionestimada, p.estado "
                    + "ORDER BY p.idprestamo DESC";

            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar préstamos por fecha: " + e.getMessage());
        }
    }

    // ===============================================================
    // 3. MÉTODO GENERAL → si hay fecha filtra, si no trae todo
    // ===============================================================
    public ResultSet buscarPrestamos(int idUsuario, String fecha) throws Exception {
        if (fecha == null) {
            return listarPrestamosDeUsuario(idUsuario);
        } else {
            return listarPrestamosPorFecha(idUsuario, fecha);
        }
    }

}
