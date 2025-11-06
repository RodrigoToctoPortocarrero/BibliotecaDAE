/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author rodri
 */
public class Usuario {

    clsJDBC con = new clsJDBC();
    String sql = "";

    //Necesito un metodo que me verifique que si el usuario existe(nombre de usuario)(para verificar que el nombre de usuario sea unico)
    public Boolean verificarUsuario(String nomusuario) throws Exception {
        try {
            sql = "SELECT * FROM USUARIO WHERE nomusuario = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nomusuario);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            throw new Exception("Ocurrio un eror al verificar usuario: " + e.getMessage());
        }
        return false;
    }

    public UsuarioSesion loginSeguro(String usu, String contrasenia) throws Exception {

        final String SAL_ESTATICO = "USAT2025";
        UsuarioSesion usuarioEncontrado = null;

        // El SQL ya es correcto, selecciona idusuario, nombre, y tipousuario.
        // Buscamos por nomusuario y contrasenia encriptada.
         sql = "SELECT idusuario, nombre, tipousuario "
                + "FROM USUARIO "
                + "WHERE nomusuario = ? AND contrasenia = MD5(? || ? || ?) AND estado = TRUE";

        Connection micon = null;
        PreparedStatement sp = null;
        ResultSet rs = null;

        try {
            micon = con.conectar();
            sp = micon.prepareStatement(sql);

            // Parámetros SQL (Correctos para la verificación)
            sp.setString(1, usu);           // 1. nomusuario (usu)
            sp.setString(2, contrasenia);   // 2. Contraseña plana
            sp.setString(3, usu);           // 3. nomusuario (usu) para el salt
            sp.setString(4, SAL_ESTATICO);  // 4. SALT estático

            rs = sp.executeQuery();

            if (rs.next()) {
                // Acceso exitoso. Capturamos los campos de la BD.
                int id = rs.getInt("idusuario");
                String nombreReal = rs.getString("nombre"); // e.g. "Ana"
                String tipo = rs.getString("tipousuario");

                // Creamos el objeto, usando 'usu' como el nomusuario
                usuarioEncontrado = new UsuarioSesion(id, nombreReal, usu, tipo); // <-- 4 parámetros!
            }

        } catch (Exception e) {
            throw new Exception("Error de base de datos durante el login: " + e.getMessage());
        } finally {
            // Bloque de cierre de recursos...
            try {
                if (rs != null) {
                    rs.close();
                }
                if (sp != null) {
                    sp.close();
                }
                if (micon != null) {
                    micon.close();
                }
            } catch (Exception e) {
                System.err.println("Advertencia: Error al cerrar recursos de conexión: " + e.getMessage());
            }
        }

        return usuarioEncontrado;
    }

    //Necesito un metodo que me permita que si el usuario y contraseña son correctos
    public String verificarUsuContra(String nomusuario, String password) throws Exception {
        try {
            sql = "SELECT nomusuario FROM USUARIO WHERE nomusuario = ? and contrasenia = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nomusuario);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("nomusuario");
            }
        } catch (Exception e) {
            throw new Exception("Ocurrio un eror al verificar usuario: " + e.getMessage());
        }

        return "";

    }

    //Necesito saber si ese usuario es lector o bibliotecario
    public String tipoUsuario(String nombreusuario) throws Exception {
        try {
            sql = "SELECT tipoUsuario from USUARIO where nomusuario = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nombreusuario);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("tipoUsuario");// Lector o Bibliotecario
            }

        } catch (Exception e) {
            throw new Exception("Ocurrio un error al ver el tipo de usuario: " + e.getMessage());
        }
        return "";
    }

    //Necesito un metodo que me permita actualizar la contraseña
    public void cambiarContrasenia(String contraseniaNueva, String nombreusuario) throws Exception {
        try {
            sql = "UPDATE USUARIO set contrasenia = ? where nomusuario= ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, contraseniaNueva);
            pst.setString(2, nombreusuario);

            pst.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Ocurrio un error al cambiar contrasenia: " + e.getMessage());
        }
    }

}
