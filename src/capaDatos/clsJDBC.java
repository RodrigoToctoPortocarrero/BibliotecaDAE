/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaDatos;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Rodrigo Tocto Portocarrero
 */
public class clsJDBC {
    
    
    private String url = "jdbc:postgresql://localhost:5432/BD_BIBLIOTECA_DAE";
    private String username = "postgres";
    private String password = "var112299";
    private Connection con = null;
    public Connection conectar() throws Exception{
        try{
           con = DriverManager.getConnection(url,username,password);
           return con;
        }catch(Exception e){
            throw new Exception("Ocurrio un error al conectar a la BD: "+e.getMessage());
        }
    }
    
    
    
}
