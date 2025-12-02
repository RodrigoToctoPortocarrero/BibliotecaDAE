/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

/**
 *
 * @author Tocto Portocarrero Rodrigo
 */
public class UsuarioSesion {

    private int idusuario;
    private String nombre;
    private String nomusuario; // <--- Nuevo campo para el login
    private String tipousuario;

    // Constructor con nomusuario
    public UsuarioSesion(int idusuario, String nombre, String nomusuario, String tipousuario) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.nomusuario = nomusuario;
        this.tipousuario = tipousuario;
    }
    
    // Getters
    public int getIdusuario() {
        return idusuario;
    }

    public String getNombre() {
        return nombre; // Nombre real (e.g., Ana)
    }

    public String getNomusuario() {
        return nomusuario; // Clave de acceso (e.g., ana.bibliotecaria)
    }

    public String getTipousuario() {
        return tipousuario;
    }

    public int getUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
