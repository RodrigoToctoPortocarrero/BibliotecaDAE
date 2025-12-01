package capaLogica;

import capaDatos.clsJDBC;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DevolucionClase {

    public boolean registrarDevolucion(String lectorNombre, String fechaReal, DefaultTableModel modelo) {

        try {
            clsJDBC db = new clsJDBC();
            db.conectar();
            Connection con = db.getCon();

            // 1) Buscar ID lector
            String sql = """
            SELECT idusuario 
            FROM usuario
            WHERE CONCAT(nombre,' ',ap_paterno,' ',ap_materno) = ?
        """;
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, lectorNombre);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                throw new Exception("Lector no encontrado.");
            }
            int idLector = rs.getInt(1);

            // 2) Préstamo activo
            sql = """
            SELECT idprestamo, fechadevolucionestimada
            FROM prestamo
            WHERE idusuariolector = ? AND estado='activo'
        """;
            pst = con.prepareStatement(sql);
            pst.setInt(1, idLector);
            rs = pst.executeQuery();

            if (!rs.next()) {
                throw new Exception("El lector no tiene préstamo activo.");
            }

            int idPrestamo = rs.getInt(1);
            Date fechaEstimada = rs.getDate(2);

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
            for (int i = 0; i < modelo.getRowCount(); i++) {

                int idEjemplar = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                String observacion = modelo.getValueAt(i, 4).toString();
                double multa = Double.parseDouble(modelo.getValueAt(i, 5).toString());

                multaTotal += multa;

                // generar ID detalle
                sql = "SELECT COALESCE(MAX(iddetalledev),0)+1 FROM detalle_devolucion";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                rs.next();
                int idDetalle = rs.getInt(1);

                sql = """
                INSERT INTO detalle_devolucion
                (iddetalledev, iddevolucion, idejemplar, observaciones, multa)
                VALUES (?,?,?,?,?)
            """;
                pst = con.prepareStatement(sql);
                pst.setInt(1, idDetalle);
                pst.setInt(2, idDevolucion);
                pst.setInt(3, idEjemplar);
                pst.setString(4, observacion);
                pst.setDouble(5, multa);
                pst.executeUpdate();

                // marcar ejemplar como devuelto
                sql = "UPDATE ejemplar SET estado_devolucion = TRUE WHERE idejemplar = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, idEjemplar);
                pst.executeUpdate();
            }

            // 6) Si ya no hay pendientes, cerrar préstamo
            sql = """
            SELECT COUNT(*)
            FROM ejemplar e
            INNER JOIN detalle_prestamo dp ON dp.idejemplar = e.idejemplar
            WHERE dp.idprestamo = ? AND e.estado_devolucion = FALSE
        """;
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

            // 7) Si hay multa → lector deja de ser vigente
            if (multaTotal > 0) {
                sql = "UPDATE usuario SET estado = FALSE WHERE idusuario = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, idLector);
                pst.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
