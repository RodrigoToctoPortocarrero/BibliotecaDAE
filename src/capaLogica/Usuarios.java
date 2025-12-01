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
            String tipoUsuario) throws Exception {

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
                + "TRUE)";

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
            Date fNacimiento, String nomUsuario, String contrasenia,
            String telefono, String correo, String direccion,
            String tipoUsuario, Boolean estado) throws Exception {

        strSQL = "UPDATE USUARIO SET "
                + "nombre = '" + nombre + "', "
                + "ap_paterno = '" + apPat + "', "
                + "ap_materno = '" + apMat + "', "
                + "f_nacimiento = '" + fNacimiento + "', "
                + "nomusuario = '" + nomUsuario + "', "
                + "contrasenia = MD5('" + contrasenia + "' || '" + nomUsuario + "' || 'USAT2025'), "
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
        try {
            strSQL = "SELECT 1 FROM PRESTAMO p "
                    + "JOIN DETALLE_PRESTAMO dp ON dp.idprestamo = p.idprestamo "
                    + "WHERE p.idusuariolector = " + id + " AND p.estado = 'activo' LIMIT 1";

            ResultSet rsPrestamos = objConectar.consultarBD(strSQL);
            if (rsPrestamos.next()) {
                throw new Exception("No se puede eliminar el usuario, tiene préstamos activos.");
            }

            strSQL = "SELECT 1 FROM MULTA WHERE idusuario = " + id + " AND pagado = FALSE LIMIT 1";
            ResultSet rsMultas = objConectar.consultarBD(strSQL);
            if (rsMultas.next()) {
                throw new Exception("No se puede eliminar el usuario, tiene multas pendientes.");
            }

            strSQL = "DELETE FROM USUARIO WHERE idusuario = " + id;
            objConectar.ejecutarBD(strSQL);

        } catch (Exception e) {
            throw new Exception("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public void darBajaUsuario(Integer id) throws Exception {
        try {
            strSQL = "SELECT 1 FROM PRESTAMO p "
                    + "JOIN DETALLE_PRESTAMO dp ON dp.idprestamo = p.idprestamo "
                    + "WHERE p.idusuariolector = " + id + " AND p.estado = 'activo' LIMIT 1";

            ResultSet rsPrestamos = objConectar.consultarBD(strSQL);
            if (rsPrestamos.next()) {
                throw new Exception("No se puede dar de baja al usuario: tiene préstamos activos.");
            }

            strSQL = "SELECT 1 FROM MULTA WHERE idusuario = " + id + " AND pagado = FALSE LIMIT 1";
            ResultSet rsMultas = objConectar.consultarBD(strSQL);
            if (rsMultas.next()) {
                throw new Exception("No se puede dar de baja al usuario: tiene multas pendientes.");
            }

            strSQL = "UPDATE USUARIO SET estado = FALSE WHERE idusuario = " + id;
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

    public Integer obtenerIdLectorPorNombre(String nombreCompleto) throws Exception {

        nombreCompleto = nombreCompleto.trim();

        strSQL = "SELECT idusuario FROM USUARIO "
                + "WHERE TRIM(CONCAT(nombre, ' ', ap_paterno, ' ', ap_materno)) ILIKE ?";

        try {
            objConectar.conectar();
            PreparedStatement ps = objConectar.getCon().prepareStatement(strSQL);
            ps.setString(1, nombreCompleto);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("idusuario");
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new Exception("Error al buscar ID del lector: " + e.getMessage());
        }
    }

    public int obtenerIdLectorPorNombreCompleto(String nombreCompleto) {
        try {
            clsJDBC db = new clsJDBC();
            db.conectar();

            String sql = """
            SELECT idusuario 
            FROM usuario 
            WHERE CONCAT(nombre, ' ', ap_paterno, ' ', ap_materno) = ?
        """;

            PreparedStatement pst = db.conectar().prepareStatement(sql);
            pst.setString(1, nombreCompleto);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

}
