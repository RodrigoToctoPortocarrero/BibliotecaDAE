/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaDatos;

import java.sql.Connection;
import java.sql.DriverManager;
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

    // Eliminamos 'sent' como variable de clase, es mejor usarlo localmente.
    // private Statement sent = null; 
    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Connection conectar() throws Exception {
        try {
            // Asegúrate de que la conexión solo se crea si es nula o está cerrada
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
     * MODIFICADO: Se elimina la desconexión en el bloque finally. La conexión
     * permanece abierta para que el ResultSet pueda ser leído. El método que
     * llama a consultarBD es responsable de llamar a desconectar.
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
        // ¡SIN BLOQUE FINALLY CON DESCONEXIÓN!
    }

    /**
     * CORRECTO: Se mantiene la desconexión en el bloque finally después de
     * ejecutar.
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
                } catch (SQLException e) {
                    /* Ignorar */ }
            }
            // Desconecta la conexión
            if (con != null) {
                desconectar();
            }
        }
    }
}
