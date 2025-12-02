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

    public ResultSet listarMultas(String fecha, String lector) throws Exception {
        // Obtenemos datos de la multa y concatenamos el nombre del usuario
        strSQL = "SELECT m.idmulta, "
                + "u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno as nombre_completo, "
                + "m.fechagenerada, "
                + "m.monto, "
                + "m.pagado "
                + "FROM MULTA m "
                + "INNER JOIN USUARIO u ON m.idusuario = u.idusuario "
                + "WHERE 1=1 ";

        if (fecha != null && !fecha.isEmpty()) {
            strSQL += "AND CAST(m.fechagenerada AS TEXT) LIKE '%" + fecha + "%' ";
        }

        // --- CAMBIO AQUÍ ---
        if (lector != null && !lector.isEmpty()) {
            // Buscamos dentro del nombre completo concatenado para que coincida con lo que se llena desde la tabla
            strSQL += "AND UPPER(u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno) LIKE UPPER('%" + lector + "%') ";
        }
        // -------------------

        strSQL += "ORDER BY m.pagado ASC, m.fechagenerada DESC";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar multas: " + e.getMessage());
        }
    }

    /**
     * Cambia el estado de la multa a PAGADO (true).
     *
     * @param idMulta ID de la multa a pagar.
     */
    public void pagarMulta(int idMulta) throws Exception {
        strSQL = "UPDATE MULTA SET pagado = TRUE WHERE idmulta = " + idMulta;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al pagar la multa: " + e.getMessage());
        }
    }
    public ResultSet listarMultasPendientes(String fechaPrestamo, String nombreLector) throws Exception {
        // Consultamos directamente la tabla MULTA unida con PRESTAMO y USUARIO
        strSQL = "SELECT m.idprestamo, "
                + "       m.idusuario, "
                + "       u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno as nombre_completo, "
                + "       p.fechaprestamo, "
                + "       m.monto as monto_total " // Alias para que coincida con tu interfaz
                + "FROM MULTA m "
                + "INNER JOIN PRESTAMO p ON m.idprestamo = p.idprestamo "
                + "INNER JOIN USUARIO u ON m.idusuario = u.idusuario "
                + "WHERE m.pagado = FALSE "; // CLAVE: Solo las que faltan pagar

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
            throw new Exception("Error al listar multas pendientes: " + e.getMessage());
        }
    }

    /**
     * Registra el pago en la tabla MULTA y actualiza el estado del usuario a vigente.
     */
    public void registrarPagoMulta(int idPrestamo, int idLector, double montoTotal) throws Exception {
        // 1. ACTUALIZAR el registro existente en MULTA a pagado = TRUE
        // Ya no insertamos porque el registro ya existe (desde tu script o trigger)
        String strUpdateMulta = "UPDATE multa "
                + "SET pagado = TRUE, fechagenerada = CURRENT_DATE "
                + "WHERE idprestamo = " + idPrestamo + " AND pagado = FALSE";
        
        // 2. Actualizar estado del lector a TRUE (Vigente/Habilitado)
        String strUpdateUsuario = "UPDATE usuario SET estado = TRUE WHERE idusuario = " + idLector;

        try {
            // Ejecutamos las actualizaciones
            objConectar.ejecutarBD(strUpdateMulta);
            objConectar.ejecutarBD(strUpdateUsuario);
        } catch (Exception e) {
            throw new Exception("Error en la transacción de pago: " + e.getMessage());
        }
    }
    

}
