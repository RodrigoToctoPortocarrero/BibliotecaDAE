/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;

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
    public Boolean verificarUsuario(String nomusuario)throws Exception{
        try{
            sql = "SELECT * FROM USUARIO WHERE nomusuario = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nomusuario);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return true;
            }
            
            
        }catch(Exception e){
            throw new Exception("Ocurrio un eror al verificar usuario: "+e.getMessage());
        }
        return false;
    }
    //Necesito un metodo que me permita que si el usuario y contraseña son correctos
    public String verificarUsuContra(String nomusuario, String password)throws Exception{
        try{
            sql = "SELECT nomusuario FROM USUARIO WHERE nomusuario = ? and contrasenia = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nomusuario);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getString("nomusuario");
            }
        }catch(Exception e){
            throw new Exception("Ocurrio un eror al verificar usuario: "+e.getMessage());
        }
        
        return "";

    }
    
    //Necesito saber si ese usuario es lector o bibliotecario
    
    public String tipoUsuario(String nombreusuario) throws Exception{
        try{
            sql = "SELECT tipoUsuario from USUARIO where nomusuario = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nombreusuario);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getString("tipoUsuario");// Lector o Bibliotecario
            }
            
        }catch(Exception e){
            throw new Exception("Ocurrio un error al ver el tipo de usuario: "+e.getMessage());
        }
        return "";
    }
    
    //Necesito un metodo que me permita actualizar la contraseña
    
    public void cambiarContrasenia(String contraseniaNueva, String nombreusuario) throws Exception{
        try{
            sql = "UPDATE USUARIO set contrasenia = ? where nomusuario= ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, contraseniaNueva);
            pst.setString(2, nombreusuario);
            
            pst.executeUpdate();
        }catch(Exception e){
            throw new Exception("Ocurrio un error al cambiar contrasenia: "+e.getMessage());
        }
    }
    
    
    
}
