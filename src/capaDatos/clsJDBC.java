package capaDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Rodrigo Tocto Portocarrero
 */
public class clsJDBC {

    private String url = "jdbc:postgresql://localhost:5432/BD_BIBLIOTECA_DAE";
    private String username = "usuario1";
    private String password = "usuario1";
    private Connection con = null;

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Connection conectar() throws Exception {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(url, username, password);
            }
            return con;
        } catch (Exception e) {
            throw new Exception("Ocurrio un error al conectar a la BD: " + e.getMessage());
        }
    }

    public void desconectar() throws Exception {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null; // Establecer a null para indicar que ya no existe
            }
        } catch (SQLException ex) {
            throw new Exception("Error al desconectar de la BD! " + ex.getMessage());
        }
    }

    /**
     * MODIFICADO: Retorna un ResultSet y deja la conexión abierta.
     */
    public ResultSet consultarBD(String strSQL) throws Exception {
        ResultSet rs = null;
        Statement sent = null; // Declarado localmente
        try {
            conectar();
            sent = con.createStatement();
            rs = sent.executeQuery(strSQL);
            return rs;
        } catch (Exception e) {
            // Si hay un error, cerramos la conexión y lanzamos la excepción.
            if (con != null) {
                desconectar();
            }
            throw new Exception("Error al ejecutar consulta: " + e.getMessage());
        }
    }

    /**
     * CORRECTO: Ejecuta DML (UPDATE, DELETE) usando Statement.
     */
    public void ejecutarBD(String strSQL) throws Exception {
        Statement sent = null; // Declarado localmente
        try {
            conectar();
            sent = con.createStatement();
            sent.executeUpdate(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al ejecutar Update --> " + e.getMessage());
        } finally {
            // Cierra el Statement para liberar recursos
            if (sent != null) {
                try {
                    sent.close();
                } catch (SQLException e) { /* Ignorar */ }
            }
            // Desconecta la conexión
            if (con != null) {
                desconectar();
            }
        }
    }
    
    // =========================================================
    // NUEVO MÉTODO PARA USAR CONSULTAS PREPARADAS (INSERT/UPDATE)
    // =========================================================

    /**
     * NUEVO: Ejecuta una sentencia DML (INSERT, UPDATE, DELETE) usando PreparedStatement.
     * Este método es crucial para manejar IDs enteros y booleanos de forma segura.
     * @param strSQL La sentencia SQL con placeholders (?).
     * @param params Los parámetros a establecer en los placeholders.
     */
    public int ejecutarBDPreparada(String strSQL, Object... params) throws Exception {
        PreparedStatement ps = null; // Usamos PreparedStatement
        int filasAfectadas = 0;
        try {
            conectar(); 
            ps = con.prepareStatement(strSQL);
            
            // Establecer los parámetros dinámicamente, manteniendo los tipos
            for (int i = 0; i < params.length; i++) {
                // ps.setObject(index, value) maneja la conversión de tipos
                ps.setObject(i + 1, params[i]); 
            }
            
            filasAfectadas = ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al ejecutar Update preparado --> " + e.getMessage());
        } finally {
            // Cierra el PreparedStatement
            if (ps != null) {
                try { 
                    ps.close(); 
                } catch (SQLException ex) { /* Ignorar */ }
            }
            // Desconecta la conexión
            if (con != null) {
                 desconectar(); 
            }
        }
        return filasAfectadas;
    }
}