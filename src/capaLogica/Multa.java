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

    public boolean verificarDeudaPendiente(int idUsuario) throws Exception {
        // CORRECCIÓN BASADA EN TU TXT: 
        // 1. La tabla es 'multa'.
        // 2. La columna del usuario es 'idusuario'.
        // 3. Para saber si debe, miramos si 'pagado' es FALSE.
        String strSQL = "SELECT COUNT(*) as total FROM multa WHERE idusuario = " + idUsuario + " AND pagado = false";

        ResultSet rs = null;
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                int cantidad = rs.getInt("total");
                return cantidad > 0; // Si hay 1 o más multas sin pagar, retorna true (es moroso)
            }
        } catch (Exception e) {
            throw new Exception("Error al verificar deuda en BD: " + e.getMessage());
        }
        return false;
    }
}
