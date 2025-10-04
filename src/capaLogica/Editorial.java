/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; // Necesario para retornar listas

/**
 *
 * @author VALENTINO
 */
public class Editorial {
    
    // ATRIBUTOS DE ACCESO A DATOS
    private clsJDBC objConectar = new clsJDBC();
    private String strSQL;
    private ResultSet rs = null;
    
    // ATRIBUTOS DE LA ENTIDAD EDITORIAL (MODELO)
    private int idEditorial;
    private String nombre;
    private String telefono;
    private String correo;
    private boolean estado; // Representa el campo 'vigencia' (activo/inactivo)

    // Constructor vacío
    public Editorial() {
    }

    // Constructor para inserciones (sin idEditorial)
    public Editorial(String nombre, String telefono, String correo, boolean estado) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.estado = estado;
    }

    // Constructor para consultas y actualizaciones (con idEditorial)
    public Editorial(int idEditorial, String nombre, String telefono, String correo, boolean estado) {
        this.idEditorial = idEditorial;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.estado = estado;
    }

    // --- MÉTODOS GETTERS Y SETTERS (Ya estaban correctos) ---
    public int getIdEditorial() { return idEditorial; }
    public void setIdEditorial(int idEditorial) { this.idEditorial = idEditorial; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    
    // =========================================================================
    // LÓGICA DE NEGOCIO (Métodos CRUD para los botones de la interfaz)
    // =========================================================================
    
    /**
     * Corresponde al botón 'Nuevo'. Inserta un nuevo registro en la BD.
     */
    public boolean insertar() throws Exception {
        // PostgreSQL requiere que el booleano se escriba como TRUE o FALSE en la consulta
        String estadoSQL = this.estado ? "TRUE" : "FALSE";
        
        // Construcción de la consulta con interpolación de String (¡cuidado con las comillas simples!)
        strSQL = String.format(
            "INSERT INTO EDITORIAL (nombre, telefono, correo, estado) VALUES ('%s', '%s', '%s', %s)",
            this.nombre,
            this.telefono,
            this.correo,
            estadoSQL
        );
        
        try {
            objConectar.ejecutarBD(strSQL);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar editorial: " + e.getMessage());
        }
    }

    /**
     * Corresponde al botón 'Modificar'. Actualiza un registro existente en la BD.
     */
    public boolean actualizar() throws Exception {
        String estadoSQL = this.estado ? "TRUE" : "FALSE";
        
        strSQL = String.format(
            "UPDATE EDITORIAL SET nombre = '%s', telefono = '%s', correo = '%s', estado = %s WHERE idEditorial = %d",
            this.nombre,
            this.telefono,
            this.correo,
            estadoSQL,
            this.idEditorial
        );
        
        try {
            objConectar.ejecutarBD(strSQL);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al actualizar editorial: " + e.getMessage());
        }
    }

    /**
     * Corresponde al botón 'Dar baja'. Realiza una BAJA LÓGICA (estado = FALSE).
     */
    public boolean darBaja() throws Exception {
        // Baja lógica: actualiza solo el campo 'estado' a FALSE.
        strSQL = "UPDATE EDITORIAL SET estado = FALSE WHERE idEditorial = " + this.idEditorial;
        
        try {
            objConectar.ejecutarBD(strSQL);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al dar de baja editorial: " + e.getMessage());
        }
    }
    
    /**
     * Corresponde al botón 'Eliminar'. Realiza una ELIMINACIÓN FÍSICA (DELETE).
     */
    public boolean eliminar() throws Exception {
        strSQL = "DELETE FROM EDITORIAL WHERE idEditorial = " + this.idEditorial;
        
        try {
            objConectar.ejecutarBD(strSQL);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al eliminar editorial: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los registros de la tabla para llenar el JTable.
     * Retorna una lista de objetos Editorial.
     */
    public List<Editorial> obtenerTodos() throws Exception {
        strSQL = "SELECT idEditorial, nombre, telefono, correo, estado FROM EDITORIAL ORDER BY nombre";
        ResultSet rsLocal = null;
        List<Editorial> lista = new ArrayList<>();
        
        try {
            rsLocal = objConectar.consultarBD(strSQL);
            
            while (rsLocal.next()) {
                // Se crea una nueva instancia de Editorial para cada fila de la BD
                Editorial e = new Editorial(); 
                e.setIdEditorial(rsLocal.getInt("idEditorial"));
                e.setNombre(rsLocal.getString("nombre"));
                e.setTelefono(rsLocal.getString("telefono"));
                e.setCorreo(rsLocal.getString("correo"));
                e.setEstado(rsLocal.getBoolean("estado"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar editoriales: " + ex.getMessage());
        } finally {
            // CRÍTICO: Debemos cerrar el ResultSet y la conexión que dejó abierta consultarBD
            if (rsLocal != null) {
                try {
                    rsLocal.close();
                } catch (SQLException ex) { /* Ignorar error al cerrar */ }
            }
            objConectar.desconectar();
        }
        return lista;
    }
    
    
    public Editorial obtenerPorId(int id) throws Exception {
    strSQL = "SELECT idEditorial, nombre, telefono, correo, estado FROM EDITORIAL WHERE idEditorial = " + id;
    ResultSet rsLocal = null;
    Editorial editorialEncontrada = null;

    try {
        rsLocal = objConectar.consultarBD(strSQL);
        
        // Si encuentra una fila (solo puede ser una)
        if (rsLocal.next()) {
            editorialEncontrada = new Editorial(); 
            editorialEncontrada.setIdEditorial(rsLocal.getInt("idEditorial"));
            editorialEncontrada.setNombre(rsLocal.getString("nombre"));
            editorialEncontrada.setTelefono(rsLocal.getString("telefono"));
            editorialEncontrada.setCorreo(rsLocal.getString("correo"));
            editorialEncontrada.setEstado(rsLocal.getBoolean("estado"));
        }
    } catch (SQLException ex) {
        throw new Exception("Error al buscar editorial por ID: " + ex.getMessage());
    } finally {
        // CRÍTICO: Debemos cerrar el ResultSet y la conexión que dejó abierta consultarBD
        if (rsLocal != null) {
            try {
                rsLocal.close();
            } catch (SQLException ex) { /* Ignorar error al cerrar */ }
        }
        objConectar.desconectar();
    }
    return editorialEncontrada;
}
}