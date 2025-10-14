package capaLogica;

import capaDatos.clsJDBC;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Valentino Lopez
 */
public class Editorial {

    private clsJDBC objJDBC = new clsJDBC(); // Instancia de la clase de conexión
    
    // Propiedades de la Editorial (Modelo de datos)
    private int ideditorial;
    private String nombre;
    private String telefono;
    private String correo;
    private boolean estado;

    // Constructores y Getters/Setters (omitiendo por brevedad, asume que existen)

    public Editorial() { }

    public int getIdeditorial() { return ideditorial; }
    public void setIdeditorial(int ideditorial) { this.ideditorial = ideditorial; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    
    // ------------------------------------
    // LÓGICA DE ACCESO A DATOS
    // ------------------------------------

    // Listar Editorial
    public ResultSet listarEditorial() throws Exception {
        Connection con = null;
        try {
            con = objJDBC.conectar();
            String sql = "SELECT ideditorial, nombre, telefono, correo, estado FROM EDITORIAL ORDER BY ideditorial";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs; // La desconexión debe hacerse después de leer el ResultSet
        } catch (Exception e) {
            // Manejo de la conexión en caso de error (similar a tu Categoria.java)
            if (con != null) {
                objJDBC.desconectar();
            }
            throw new Exception("Error al listar editoriales: " + e.getMessage());
        }
    }

    // Buscar Editorial
    public ResultSet buscarPorCodigo(int ideditorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "SELECT ideditorial, nombre, telefono, correo, estado FROM EDITORIAL WHERE ideditorial = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, ideditorial);
            
            return pst.executeQuery(); // La desconexión debe hacerse después de leer el ResultSet
        } catch (Exception e) {
            if (con != null) {
                objJDBC.desconectar();
            }
            throw new Exception("Error al buscar editorial: " + e.getMessage());
        }
    }

    
    public void registrar(Editorial editorial) throws Exception {
    // 1. Definimos la SQL con placeholders (?) para todas las columnas
    String sql = "INSERT INTO EDITORIAL (ideditorial, nombre, telefono, correo, estado) " +
                 "VALUES (?, ?, ?, ?, ?)";
    
    Connection con = null;
    PreparedStatement pst = null;

    try {
        // Asumo que 'objJDBC' es tu instancia de la clase de conexión/utilidad
        con = objJDBC.conectar(); 
        pst = con.prepareStatement(sql);
        
        // 2. Asignamos los valores a los placeholders
        pst.setInt(1, editorial.getIdeditorial()); // CLAVE PRIMARIA
        pst.setString(2, editorial.getNombre());
        pst.setString(3, editorial.getTelefono());
        pst.setString(4, editorial.getCorreo());
        pst.setBoolean(5, editorial.isEstado());
        
        // 3. Ejecutamos la inserción
        int filasAfectadas = pst.executeUpdate();
        
        if (filasAfectadas == 0) {
            throw new Exception("No se insertó la Editorial. Verifique que el ID no exista y que la conexión sea válida.");
        }
        
    } catch (SQLException e) {
        // Esto es lo que captura el error de clave duplicada o de valor NULL
        throw new Exception("Error de base de datos al registrar editorial: " + e.getMessage());
    } catch (Exception e) {
        // Capturamos cualquier otro error
        throw new Exception("Error al registrar editorial: " + e.getMessage());
    } finally {
        // 4. Cerramos recursos
        if (pst != null) pst.close();
        if (con != null) objJDBC.desconectar();
    }
}

    // Modificar Editorial (Actualizar)
    public void modificar(Editorial editorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "UPDATE EDITORIAL SET nombre=?, telefono=?, correo=?, estado=? WHERE ideditorial=?";
            
            pst = con.prepareStatement(sql);
            
            pst.setString(1, editorial.getNombre());
            pst.setString(2, editorial.getTelefono());
            pst.setString(3, editorial.getCorreo());
            pst.setBoolean(4, editorial.isEstado());
            pst.setInt(5, editorial.getIdeditorial()); // Condición WHERE
            
            pst.executeUpdate();
            
        } catch (Exception e) {
            throw new Exception("Error al modificar editorial: " + e.getMessage());
        } finally {
            if (pst != null) pst.close();
            if (con != null) objJDBC.desconectar(); // Cierre de recursos
        }
    }
    
    // Dar Baja Editorial (Cambiar estado a FALSE)
    public void darBaja(int ideditorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "UPDATE EDITORIAL SET estado=FALSE WHERE ideditorial= ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, ideditorial);
            pst.executeUpdate();
            
        } catch (Exception e) {
            throw new Exception("Error al dar de baja editorial: " + e.getMessage());
        } finally {
            if (pst != null) pst.close();
            if (con != null) objJDBC.desconectar(); 
        }
    }
    
    // Eliminar Editorial (Físicamente)
    public void eliminar(int ideditorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "DELETE FROM EDITORIAL WHERE ideditorial = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, ideditorial);
            pst.executeUpdate();
            
        } catch (Exception e) {
            throw new Exception("Error al eliminar editorial: " + e.getMessage());
        } finally {
            if (pst != null) pst.close();
            if (con != null) objJDBC.desconectar(); 
        }
    }
    
    public ResultSet listarEditorialesActivas() throws Exception {
        Connection con = null;
        Statement st = null;
        try {
            con = objJDBC.conectar();
            String sql = "SELECT ideditorial, nombre FROM EDITORIAL WHERE estado = TRUE ORDER BY nombre";
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs; 
        } catch (Exception e) {
            // Cierra la conexión si hay error, ya que el ResultSet no se generó.
            if (con != null) {
                objJDBC.desconectar();
            }
            // Intenta cerrar el Statement si se abrió
            if (st != null) {
                 try { st.close(); } catch (SQLException ex) { /* ignorar */ }
            }
            throw new Exception("Error al listar editoriales activas: " + e.getMessage());
        }
    }
}