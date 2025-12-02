package capaLogica;

import capaDatos.clsJDBC;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DevolucionClase {

    public boolean registrarDevolucion(String lectorNombre, String fechaReal, DefaultTableModel modelo) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        clsJDBC db = new clsJDBC();

        try {
            db.conectar();
            con = db.getCon();

            // 1. DESACTIVAR EL AUTO-GUARDADO (INICIO DE TRANSACCIÓN)
            con.setAutoCommit(false);

            // --- LÓGICA DE NEGOCIO ---
            // 1) Buscar ID lector
            String sql = "SELECT idusuario FROM usuario WHERE CONCAT(nombre,' ',ap_paterno,' ',ap_materno) = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, lectorNombre);
            rs = pst.executeQuery();

            if (!rs.next()) {
                throw new Exception("Lector no encontrado.");
            }
            int idLector = rs.getInt(1);

            // 2) Préstamo activo
            sql = "SELECT idprestamo FROM prestamo WHERE idusuariolector = ? AND estado='activo'";
            pst = con.prepareStatement(sql);
            pst.setInt(1, idLector);
            rs = pst.executeQuery();

            if (!rs.next()) {
                throw new Exception("El lector no tiene préstamo activo.");
            }
            int idPrestamo = rs.getInt(1);

            // 3) ID de devolución
            sql = "SELECT COALESCE(MAX(iddevolucion),0)+1 FROM devolucion";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            int idDevolucion = rs.getInt(1);

            // 4) Insert devolución
            sql = "INSERT INTO devolucion VALUES(?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, idDevolucion);
            pst.setInt(2, idPrestamo);
            pst.setDate(3, java.sql.Date.valueOf(fechaReal));
            pst.executeUpdate();

            double multaTotal = 0;

            // 5) Detalles de devolución
            // Preparar statements fuera del bucle mejora el rendimiento
            String sqlIdDet = "SELECT COALESCE(MAX(iddetalledev),0)+1 FROM detalle_devolucion";
            String sqlInsDet = "INSERT INTO detalle_devolucion (iddetalledev, iddevolucion, idejemplar, observaciones, multa) VALUES (?,?,?,?,?)";
            String sqlUpdEjemplar = "UPDATE ejemplar SET estado_devolucion = TRUE WHERE idejemplar = ?";

            PreparedStatement pstIdDet = con.prepareStatement(sqlIdDet);
            PreparedStatement pstInsDet = con.prepareStatement(sqlInsDet);
            PreparedStatement pstUpdEjemplar = con.prepareStatement(sqlUpdEjemplar);

            for (int i = 0; i < modelo.getRowCount(); i++) {
                int idEjemplar = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                String observacion = modelo.getValueAt(i, 4).toString();
                double multa = Double.parseDouble(modelo.getValueAt(i, 5).toString());
                multaTotal += multa;

                // Generar ID detalle
                rs = pstIdDet.executeQuery();
                rs.next();
                int idDetalle = rs.getInt(1);

                // Insertar detalle
                pstInsDet.setInt(1, idDetalle);
                pstInsDet.setInt(2, idDevolucion);
                pstInsDet.setInt(3, idEjemplar);
                pstInsDet.setString(4, observacion);
                pstInsDet.setDouble(5, multa);
                pstInsDet.executeUpdate();

                // Actualizar ejemplar (Aquí puedes usar la lógica que vimos antes: 'situacion' o 'estado_devolucion')
                pstUpdEjemplar.setInt(1, idEjemplar);
                pstUpdEjemplar.executeUpdate();
            }

            // 6) Verificar si el préstamo se completó
            sql = "SELECT COUNT(*) FROM ejemplar e "
                    + "INNER JOIN detalle_prestamo dp ON dp.idejemplar = e.idejemplar "
                    + "WHERE dp.idprestamo = ? AND e.estado_devolucion = FALSE";
            pst = con.prepareStatement(sql);
            pst.setInt(1, idPrestamo);
            rs = pst.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                sql = "UPDATE prestamo SET estado='completado' WHERE idprestamo=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, idPrestamo);
                pst.executeUpdate();
            }

            // 7) Aplicar multa si existe
            if (multaTotal > 0) {
                // OJO: Aquí estás desactivando al usuario. Asegúrate que eso es lo que quieres.
                sql = "UPDATE usuario SET estado = FALSE WHERE idusuario = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, idLector);
                pst.executeUpdate();
            }

            // 2. CONFIRMAR TODOS LOS CAMBIOS (COMMIT)
            // Si llegamos aquí sin errores, guardamos todo de golpe.
            con.commit();
            return true;

        } catch (Exception e) {
            // 3. DESHACER CAMBIOS EN CASO DE ERROR (ROLLBACK)
            if (con != null) {
                try {
                    System.err.println("Error en transacción, deshaciendo cambios...");
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Error en devolución: " + e.getMessage());
        } finally {
            // 4. SIEMPRE CERRAR CONEXIONES Y REACTIVAR AUTOCOMMIT
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.setAutoCommit(true); // Buena práctica dejar la conexión como estaba
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
