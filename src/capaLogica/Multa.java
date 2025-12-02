/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author Edgar Bernabe
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
                + "    p.idprestamo, "
                + "    u.idusuario "
                + "FROM DETALLE_DEVOLUCION dd "
                + "INNER JOIN EJEMPLAR e ON dd.idejemplar = e.idejemplar "
                + "INNER JOIN DEVOLUCION d ON dd.iddevolucion = d.iddevolucion "
                + "INNER JOIN PRESTAMO p ON d.idprestamo = p.idprestamo "
                + "INNER JOIN USUARIO u ON p.idusuariolector = u.idusuario "
                + "WHERE dd.multa > 0 ";

        if (fechaPrestamo != null) {
            strSQL += "AND p.fechaprestamo = '" + fechaPrestamo + "' ";
        }

        if (nombreLector != null && !nombreLector.isEmpty()) {
            strSQL += "AND UPPER(u.nombre || ' ' || u.ap_paterno || ' ' || u.ap_materno) LIKE UPPER('%" + nombreLector + "%') ";
        }

        // **CRÍTICO: Validar que NO exista multa pagada Y que el usuario esté inhabilitado**
        strSQL += "AND NOT EXISTS ( "
                + "    SELECT 1 FROM MULTA m "
                + "    WHERE m.idprestamo = p.idprestamo "
                + "    AND m.pagado = TRUE "
                + ") "
                + "AND u.estado = FALSE "; // **AGREGAR ESTA LÍNEA**

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar detalles: " + e.getMessage());
        }
    }

    /**
     * Paga la multa registrando en la tabla MULTA y habilitando al usuario.
     */
    public void pagarMulta(int idPrestamo, int idUsuario, double montoTotal) throws Exception {
        try {
            // **PASO 1**: Verificar si ya existe un registro en MULTA
            String strVerificar = "SELECT COUNT(*) as total FROM multa WHERE idprestamo = " + idPrestamo;
            ResultSet rsVerificar = objConectar.consultarBD(strVerificar);

            boolean existeRegistro = false;
            if (rsVerificar.next()) {
                existeRegistro = rsVerificar.getInt("total") > 0;
            }

            // **PASO 2**: INSERT o UPDATE según corresponda
            if (existeRegistro) {
                // Ya existe, solo actualizar
                String strUpdate = "UPDATE multa "
                        + "SET pagado = TRUE, "
                        + "    monto = " + montoTotal + ", "
                        + "    fechagenerada = CURRENT_DATE "
                        + "WHERE idprestamo = " + idPrestamo;
                objConectar.ejecutarBD(strUpdate);
            } else {
                // No existe, insertar nuevo registro
                String strObtenerMaxId = "SELECT COALESCE(MAX(idmulta), 0) + 1 as nuevo_id FROM multa";
                ResultSet rsId = objConectar.consultarBD(strObtenerMaxId);
                int nuevoId = 1;
                if (rsId.next()) {
                    nuevoId = rsId.getInt("nuevo_id");
                }

                String strInsert = "INSERT INTO multa (idmulta, idusuario, idprestamo, monto, pagado, fechagenerada) "
                        + "VALUES (" + nuevoId + ", " + idUsuario + ", " + idPrestamo + ", "
                        + montoTotal + ", TRUE, CURRENT_DATE)";
                objConectar.ejecutarBD(strInsert);
            }

            // **PASO 3**: Habilitar al usuario (cambiar estado a TRUE)
            String strUpdateUsuario = "UPDATE usuario SET estado = TRUE WHERE idusuario = " + idUsuario;
            objConectar.ejecutarBD(strUpdateUsuario);

        } catch (Exception e) {
            throw new Exception("Error al pagar multa: " + e.getMessage());
        }
    }

}
