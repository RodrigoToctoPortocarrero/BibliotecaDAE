package capaLogica;

import capaDatos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Usuario {

    clsJDBC con = new clsJDBC();
    String sql = "";
    final String SAL_ESTATICO = "USAT2025";

    // ============================================================
    // VERIFICAR SI EL NOMBRE DE USUARIO EXISTE
    // ============================================================
    public Boolean verificarUsuario(String nomusuario) throws Exception {
        try {
            sql = "SELECT 1 FROM USUARIO WHERE nomusuario = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nomusuario);
            ResultSet rs = pst.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new Exception("Error al verificar usuario: " + e.getMessage());
        }
    }

    // ============================================================
    // LOGIN SEGURO (ya estaba bien)
    // ============================================================
    public UsuarioSesion loginSeguro(String usu, String contrasenia) throws Exception {

        UsuarioSesion usuarioEncontrado = null;

        sql = "SELECT idusuario, nombre, tipousuario "
                + "FROM USUARIO "
                + "WHERE nomusuario = ? "
                + "AND contrasenia = MD5(? || ? || ?) "
                + "AND estado = TRUE";

        try (Connection micon = con.conectar(); PreparedStatement sp = micon.prepareStatement(sql)) {

            sp.setString(1, usu);
            sp.setString(2, contrasenia);
            sp.setString(3, usu);
            sp.setString(4, SAL_ESTATICO);

            ResultSet rs = sp.executeQuery();

            if (rs.next()) {
                usuarioEncontrado = new UsuarioSesion(
                        rs.getInt("idusuario"),
                        rs.getString("nombre"),
                        usu,
                        rs.getString("tipousuario")
                );
            }

        } catch (Exception e) {
            throw new Exception("Error en login: " + e.getMessage());
        }

        return usuarioEncontrado;
    }

    // ============================================================
    // OBTENER TIPO DE USUARIO
    // ============================================================
    public String tipoUsuario(String nombreusuario) throws Exception {
        try {
            sql = "SELECT tipoUsuario FROM USUARIO WHERE nomusuario = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nombreusuario);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("tipoUsuario");
            }

        } catch (Exception e) {
            throw new Exception("Error al obtener tipo de usuario: " + e.getMessage());
        }
        return "";
    }

    // ============================================================
    // NUEVO: VERIFICAR CONTRASEÑA ACTUAL (REQUERIDO ANTES DE CAMBIAR)
    // ============================================================
    public boolean verificarContraActual(String nomusuario, String contraActual) throws Exception {

        sql = "SELECT 1 FROM USUARIO "
                + "WHERE nomusuario = ? "
                + "AND contrasenia = MD5(? || ? || ?)";

        try (Connection micon = con.conectar(); PreparedStatement pst = micon.prepareStatement(sql)) {

            pst.setString(1, nomusuario);
            pst.setString(2, contraActual);
            pst.setString(3, nomusuario);
            pst.setString(4, SAL_ESTATICO);

            ResultSet rs = pst.executeQuery();

            return rs.next();  // Si existe → contraseña actual válida

        } catch (Exception e) {
            throw new Exception("Error al verificar contraseña actual: " + e.getMessage());
        }
    }

    public boolean esBibliotecario(String usuario) throws Exception {
        String sql = "SELECT 1 FROM USUARIO WHERE nomusuario = ? AND tipousuario = 'bibliotecario'";

        try (Connection c = con.conectar(); PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setString(1, usuario);
            ResultSet rs = pst.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new Exception("Error al validar tipo de usuario: " + e.getMessage());
        }
    }

    // ============================================================
    // CAMBIAR CONTRASEÑA (USANDO EL MISMO HASH DEL LOGIN)
    // ============================================================
    public void cambiarContrasenia(String contraseniaNueva, String nombreusuario) throws Exception {

        if (!esBibliotecario(nombreusuario)) {
            throw new Exception("Solo los bibliotecarios pueden cambiar contraseña.");
        }

        sql = "UPDATE USUARIO SET contrasenia = MD5(? || ? || ?) WHERE nomusuario = ?";

        try (Connection micon = con.conectar(); PreparedStatement pst = micon.prepareStatement(sql)) {

            pst.setString(1, contraseniaNueva);
            pst.setString(2, nombreusuario);
            pst.setString(3, SAL_ESTATICO);
            pst.setString(4, nombreusuario);

            pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al cambiar contraseña: " + e.getMessage());
        }
    }
}
