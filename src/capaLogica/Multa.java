/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author BIENVENIDO
 */
public class Multa {

    int idPrestamoGlobal = -1;
    int idUsuarioGlobal = -1;
    double totalMultaGlobal = 0.0;
    private clsJDBC objConectar = new clsJDBC();
    private String strSQL;
    private ResultSet rs = null;

   public ResultSet listarMultasPendientes(String fechaPrestamo, String nombreLector) throws Exception {
        strSQL = "SELECT m.idmulta, "
                + "       m.idprestamo, "
                + "       u.idusuario, "
                + "       u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno as nombre_completo, "
                + "       p.fechaprestamo, "
                + "       dd.observaciones, "
                + "       dd.estado as estado_libro, " // Estado del libro al devolver (True=Bien, etc)
                + "       dd.multa as monto_item, "   // Multa individual por ese libro
                + "       m.pagado "
                + "FROM MULTA m "
                + "INNER JOIN PRESTAMO p ON m.idprestamo = p.idprestamo "
                + "INNER JOIN USUARIO u ON m.idusuario = u.idusuario "
                + "INNER JOIN DEVOLUCION d ON p.idprestamo = d.idprestamo "
                + "INNER JOIN DETALLE_DEVOLUCION dd ON d.iddevolucion = dd.iddevolucion "
                + "WHERE m.pagado = FALSE AND dd.multa > 0 "; 

        // Filtro por fecha de PRÉSTAMO
        if (fechaPrestamo != null && !fechaPrestamo.isEmpty()) {
            strSQL += "AND CAST(p.fechaprestamo AS TEXT) LIKE '%" + fechaPrestamo + "%' ";
        }

        // Filtro por nombre de lector
        if (nombreLector != null && !nombreLector.isEmpty()) {
            strSQL += "AND UPPER(u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno) LIKE UPPER('%" + nombreLector + "%') ";
        }

        strSQL += "ORDER BY p.fechaprestamo ASC";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar detalles de multas: " + e.getMessage());
        }
    }

    /**
     * Registra el pago ACTUALIZANDO la tabla MULTA y habilitando al usuario.
     * NOTA: Al pagar por ID de préstamo, se limpian todos los detalles asociados a ese préstamo.
     */
    public void registrarPagoMulta(int idPrestamo, int idLector) throws Exception {
        // 1. Marcar la multa general del préstamo como pagada
        String strUpdateMulta = "UPDATE multa "
                + "SET pagado = TRUE, fechagenerada = CURRENT_DATE "
                + "WHERE idprestamo = " + idPrestamo + " AND pagado = FALSE";
        
        // 2. Habilitar al usuario
        String strUpdateUsuario = "UPDATE usuario SET estado = TRUE WHERE idusuario = " + idLector;

        try {
            objConectar.ejecutarBD(strUpdateMulta);
            objConectar.ejecutarBD(strUpdateUsuario);
        } catch (Exception e) {
            throw new Exception("Error en la transacción de pago: " + e.getMessage());
        }
    }
    

}
