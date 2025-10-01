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
    private Statement sent = null;

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Connection conectar() throws Exception {
        try {
            con = DriverManager.getConnection(url, username, password);
            return con;
        } catch (Exception e) {
            throw new Exception("Ocurrio un error al conectar a la BD: " + e.getMessage());
        }
    }

    public void desconectar() throws Exception {
        try {
            con.close();
        } catch (SQLException ex) {
            throw new Exception("Error al desconectar de la BD!" + ex.getMessage());
        }
    }

    public ResultSet consultarBD(String strSQL) throws Exception {
        ResultSet rs = null;
        try {
            conectar();
            sent = con.createStatement();
            rs = sent.executeQuery(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al ejecutar consulta" + e.getMessage());
        } finally {
            if (con != null) {
                desconectar();
            }
        }
    }

    public void ejecutarBD(String strSQL) throws Exception {
        try {
            conectar();
            sent = con.createStatement();
            sent.executeUpdate(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al ejecutar Update --> " + e.getMessage());
        } finally {
            if (con != null) {
                desconectar();
            }
        }
    }
}
