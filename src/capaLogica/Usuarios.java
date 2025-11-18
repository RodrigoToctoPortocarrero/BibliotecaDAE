package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;

/**
 *
 * @author FABIAN VERA
 */
public class Usuarios {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs;

    public ResultSet listarUsuarios() throws Exception {
        strSQL = "SELECT * FROM USUARIO";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios: " + e.getMessage());
        }
    }

    public Integer generarCodigoUsuario() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idusuario),0)+1 AS codigo FROM USUARIO";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de usuario: " + e.getMessage());
        }
        return 0;
    }

    public void registrar(Integer id, String nombre, String apPat, String apMat,
            String fNacimiento, String nomUsuario, String contrasenia,
            String telefono, String correo, String direccion,
            String tipoUsuario, Boolean estado) throws Exception {

        strSQL = "INSERT INTO USUARIO (idusuario, nombre, ap_paterno, ap_materno, f_nacimiento, "
                + "nomusuario, contrasenia, telefono, correo, direccion, tipousuario, estado) "
                + "VALUES ("
                + id + ", '"
                + nombre + "', '"
                + apPat + "', '"
                + apMat + "', '"
                + fNacimiento + "', '"
                + nomUsuario + "', "
                + "MD5('" + contrasenia + "' || '" + nomUsuario + "' || 'USAT2025'), '"
                + telefono + "', '"
                + correo + "', '"
                + direccion + "', '"
                + tipoUsuario + "', "
                + estado + ")";

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar usuario: " + e.getMessage());
        }
    }

    public ResultSet buscarUsuario(Integer id) throws Exception {
        strSQL = "SELECT * FROM USUARIO WHERE idusuario = " + id;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario: " + e.getMessage());
        }
    }

    public ResultSet buscarUsuarioPorNombre(String nomUsuario) throws Exception {
        strSQL = "SELECT * FROM USUARIO WHERE nomusuario = '" + nomUsuario + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario por nombre: " + e.getMessage());
        }
    }

    public void modificar(Integer id, String nombre, String apPat, String apMat,
            Date fNacimiento, String nomUsuario,
            String telefono, String correo, String direccion,
            String tipoUsuario, Boolean estado) throws Exception {

        strSQL = "UPDATE USUARIO SET "
                + "nombre = '" + nombre + "', "
                + "ap_paterno = '" + apPat + "', "
                + "ap_materno = '" + apMat + "', "
                + "f_nacimiento = '" + fNacimiento + "', "
                + "nomusuario = '" + nomUsuario + "', "
                + "telefono = '" + telefono + "', "
                + "correo = '" + correo + "', "
                + "direccion = '" + direccion + "', "
                + "tipousuario = '" + tipoUsuario + "', "
                + "estado = " + estado + " "
                + "WHERE idusuario = " + id;

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(Integer id) throws Exception {
        strSQL = "DELETE FROM USUARIO WHERE idusuario=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public void darBajaUsuario(Integer id) throws Exception {
        strSQL = "UPDATE USUARIO SET estado=false WHERE idusuario=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja usuario: " + e.getMessage());
        }
    }

    public ResultSet listarPorTipo(String tipo) throws Exception {
        strSQL = "SELECT * FROM USUARIO WHERE tipousuario='" + tipo + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios por tipo: " + e.getMessage());
        }
    }
}
