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

 public ResultSet buscarDetallesDeuda(java.sql.Date fechaPrestamo, String nombreLector) throws Exception {
        strSQL = "SELECT "
                + "    e.NroEjemplar, "
                + "    dd.multa, "
                + "    dd.observaciones, "
                + "    u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno as nombre_completo, "
                + "    p.idprestamo, " // Guardamos esto oculto para saber qué préstamo se está pagando
                + "    u.idusuario "   // Guardamos esto oculto para habilitar al usuario luego
                + "FROM DETALLE_DEVOLUCION dd "
                + "INNER JOIN EJEMPLAR e ON dd.idejemplar = e.idejemplar "
                + "INNER JOIN DEVOLUCION d ON dd.iddevolucion = d.iddevolucion "
                + "INNER JOIN PRESTAMO p ON d.idprestamo = p.idprestamo "
                + "INNER JOIN USUARIO u ON p.idusuariolector = u.idusuario "
                + "LEFT JOIN MULTA m ON p.idprestamo = m.idprestamo " // Verificamos estado en tabla MULTA
                + "WHERE dd.multa > 0 "
                + "AND (m.pagado IS NULL OR m.pagado = FALSE) "; // Solo mostrar si NO está pagado

        // Filtro 1: Fecha del Préstamo (Dato del DateChooser)
        if (fechaPrestamo != null) {
            strSQL += "AND p.fechaprestamo = '" + fechaPrestamo + "' ";
        }

        // Filtro 2: Nombre del Lector (Dato del Txt)
        if (nombreLector != null && !nombreLector.isEmpty()) {
            strSQL += "AND UPPER(u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno) LIKE UPPER('%" + nombreLector + "%') ";
        }

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar en detalle_devolucion: " + e.getMessage());
        }
    }

    /**
     * Paga la multa registrando en la tabla MULTA y habilitando al usuario.
     */
    public void pagarMulta(int idPrestamo, int idUsuario, double montoTotal) throws Exception {
        // 1. Actualizar/Insertar en tabla MULTA como PAGADO
        // Usamos un UPDATE porque tu sistema ya genera la multa en estado FALSE previamente.
        String strUpdateMulta = "UPDATE multa "
                + "SET pagado = TRUE, "
                + "    monto = " + montoTotal + ", "
                + "    fechagenerada = CURRENT_DATE "
                + "WHERE idprestamo = " + idPrestamo;

        // 2. Desbloquear al Usuario
        String strUpdateUsuario = "UPDATE usuario SET estado = TRUE WHERE idusuario = " + idUsuario;

        try {
            objConectar.ejecutarBD(strUpdateMulta);
            objConectar.ejecutarBD(strUpdateUsuario);
        } catch (Exception e) {
            throw new Exception("Error en la transacción de pago: " + e.getMessage());
        }
    }
    

}
