package capaLogica;

import capaDatos.clsJDBC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JTable;

/**
 * @author Fabian Antonio Carrasco Vera
 */
public class Prestamo {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;
    Connection con = null;
    Statement sent;
    PreparedStatement pst = null;

    public Integer generarCodigoPrestamo() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idprestamo),0)+1 AS codigo FROM prestamo";
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
        strSQL = "SELECT COALESCE(MAX(iddetalle),0)+1 AS codigo FROM detalle_prestamo";
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
    public boolean usuarioEstaVigente(int idLector) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            strSQL = "SELECT estado FROM usuario WHERE idusuario = ? AND tipousuario = 'lector'";
            pst = objConectar.conectar().prepareStatement(strSQL);
            pst.setInt(1, idLector);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("estado"); 
            } else {
                throw new Exception("El usuario no existe o no es un lector.");
            }

        } catch (Exception e) {
            throw new Exception("Error al verificar vigencia del lector: " + e.getMessage());
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception ex) {
            }
            if (pst != null) try {
                pst.close();
            } catch (Exception ex) {
            }
        }
    }

    public boolean tienePrestamosActivos(int idLector) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            strSQL = "SELECT 1 FROM prestamo "
                    + "WHERE idusuariolector = ? AND estado = 'activo' "
                    + "LIMIT 1";

            pst = objConectar.conectar().prepareStatement(strSQL);
            pst.setInt(1, idLector);
            rs = pst.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new Exception("Error al verificar préstamos activos: " + e.getMessage());
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception ex) {
            }
            if (pst != null) try {
                pst.close();
            } catch (Exception ex) {
            }
        }
    }

    public boolean tieneMultasPendientes(int idLector) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            strSQL = "SELECT 1 FROM multa "
                    + "WHERE idusuario = ? AND pagado = false "
                    + "LIMIT 1";

            pst = objConectar.conectar().prepareStatement(strSQL);
            pst.setInt(1, idLector);
            rs = pst.executeQuery();

            return rs.next(); 

        } catch (Exception e) {
            throw new Exception("Error al verificar multas pendientes: " + e.getMessage());
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception ex) {
            }
            if (pst != null) try {
                pst.close();
            } catch (Exception ex) {
            }
        }
    }

    public void registrarPrestamo(
            Integer idPrestamo,
            Integer idLector,
            Integer idBibliotecario,
            java.sql.Date fechaEstimado, 
            JTable tabla) throws Exception {

        PreparedStatement pstPrestamo = null;
        PreparedStatement pstDetalle = null;
        PreparedStatement pstEjemplar = null;

        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false); 


            if (!usuarioEstaVigente(idLector)) {
                throw new Exception("El usuario lector no está vigente. No puede realizar préstamos.");
            }

            int filas = tabla.getRowCount();
            if (filas == 0) {
                throw new Exception("Debe agregar al menos un ejemplar al préstamo.");
            }

            strSQL = "INSERT INTO prestamo (idprestamo, idusuariolector, idusuariobibliotecario, "
                    + "fechaprestamo, fechadevolucionestimada, estado) "
                    + "VALUES (?, ?, ?, CURRENT_DATE, ?, 'activo')";

            pstPrestamo = con.prepareStatement(strSQL);
            pstPrestamo.setInt(1, idPrestamo);
            pstPrestamo.setInt(2, idLector);
            pstPrestamo.setInt(3, idBibliotecario);
            pstPrestamo.setDate(4, fechaEstimado); 
            pstPrestamo.executeUpdate();


            strSQL = "INSERT INTO detalle_prestamo "
                    + "(iddetalle, idprestamo, idejemplar, observaciones, estado) "
                    + "VALUES (?, ?, ?, ?, TRUE)";
            pstDetalle = con.prepareStatement(strSQL);

            strSQL = "UPDATE ejemplar SET estado_devolucion = FALSE WHERE idejemplar = ?";
            
            pstEjemplar = con.prepareStatement(strSQL);

            for (int i = 0; i < filas; i++) {

                Integer iddetalle = generarCodigoDetalle();
                Integer idejemplar = Integer.parseInt(tabla.getValueAt(i, 0).toString());

                String obs = "";
                if (tabla.getValueAt(i, 3) != null) {
                    obs = tabla.getValueAt(i, 3).toString().trim();
                }

                pstDetalle.setInt(1, iddetalle);
                pstDetalle.setInt(2, idPrestamo);
                pstDetalle.setInt(3, idejemplar);
                pstDetalle.setString(4, obs);
                pstDetalle.executeUpdate();

                pstEjemplar.setInt(1, idejemplar);
                pstEjemplar.executeUpdate();
            }

            con.commit();

        } catch (Exception e) {

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
                if (pstPrestamo != null) {
                    pstPrestamo.close();
                }
                if (pstDetalle != null) {
                    pstDetalle.close();
                }
                if (pstEjemplar != null) {
                    pstEjemplar.close();
                }
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (Exception ex) {
                System.err.println("Error al cerrar recursos: " + ex.getMessage());
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


    public ResultSet buscarPrestamos(int idUsuario, String fecha) throws Exception {
        if (fecha == null || fecha.trim().isEmpty()) {
            return listarPrestamosDeUsuario(idUsuario);
        } else {
            return listarPrestamosPorFecha(idUsuario, fecha);
        }
    }
}
