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

  public ResultSet buscarMultasDetalladas(java.sql.Date fechaPrestamo, String nombreLector) throws Exception {
        strSQL = "SELECT "
                + "    e.NroEjemplar, "
                + "    dd.multa, "
                + "    dd.observaciones, "
                + "    u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno as lector_completo, "
                + "    p.idprestamo, " // Oculto (necesario para pagar)
                + "    u.idusuario "   // Oculto (necesario para habilitar)
                + "FROM DETALLE_DEVOLUCION dd "
                + "INNER JOIN EJEMPLAR e ON dd.idejemplar = e.idejemplar "
                + "INNER JOIN DEVOLUCION d ON dd.iddevolucion = d.iddevolucion "
                + "INNER JOIN PRESTAMO p ON d.idprestamo = p.idprestamo "
                + "INNER JOIN USUARIO u ON p.idusuariolector = u.idusuario "
                + "INNER JOIN MULTA m ON p.idprestamo = m.idprestamo " // Unimos con multa para verificar estado
                + "WHERE dd.multa > 0 "
                + "AND m.pagado = FALSE "; // Solo lo que no se ha pagado

        // Filtro obligatorio por Fecha de Préstamo
        if (fechaPrestamo != null) {
            strSQL += "AND p.fechaprestamo = '" + fechaPrestamo + "' ";
        }

        // Filtro por nombre concatenado
        if (nombreLector != null && !nombreLector.isEmpty()) {
            strSQL += "AND UPPER(u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno) LIKE UPPER('%" + nombreLector + "%') ";
        }

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar detalles de multas: " + e.getMessage());
        }
    }

    /**
     * Transacción de Pago:
     * 1. Actualiza la tabla MULTA (Monto total, pagado=true, fecha actual).
     * 2. Actualiza la tabla USUARIO (estado=true).
     */
    public void realizarPago(int idPrestamo, int idUsuario, double montoTotal) throws Exception {
        // 1. Actualizar la Multa a PAGADO
        String strUpdateMulta = "UPDATE multa "
                + "SET pagado = TRUE, "
                + "    monto = " + montoTotal + ", "
                + "    fechagenerada = CURRENT_DATE "
                + "WHERE idprestamo = " + idPrestamo;

        // 2. Habilitar al Lector (Estado = TRUE)
        String strUpdateUsuario = "UPDATE usuario SET estado = TRUE WHERE idusuario = " + idUsuario;

        try {
            // Ejecutar ambas consultas
            objConectar.ejecutarBD(strUpdateMulta);
            objConectar.ejecutarBD(strUpdateUsuario);
        } catch (Exception e) {
            throw new Exception("Error en la transacción de pago: " + e.getMessage());
        }
    }
    

}
