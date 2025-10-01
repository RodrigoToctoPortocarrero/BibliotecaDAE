/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author Percy Alexander
 */
public class Libro {
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;


    public ResultSet listarLibros() throws Exception {
        strSQL = "SELECT * FROM Libros";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar libros: " + e.getMessage());
        }
    }

    public Integer generarCodigoLibro() throws Exception {
        strSQL = "SELECT COALESCE(MAX(id_libro), 0) + 1 AS codigo FROM Libros";
        try {
            rs = objConectar.consultarBD(strSQL);
            while (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de libro");
        }
        return 0;
    }


    public void registrar(Integer id, String titulo, String autor, String categoria,
            Integer anioPublicacion, Boolean estadoVigencia) throws Exception {

        strSQL = "INSERT INTO Libros (id_libro, titulo, autor_completo, categoria, anio_publicacion, estado_vigencia) VALUES ("
                + id + ", '" + titulo + "', '" + autor + "', '"
                + categoria + "', " + anioPublicacion + ", " + estadoVigencia + ")";

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar libro: " + e.getMessage());
        }
    }

    public ResultSet buscarLibro(Integer id) throws Exception {
        strSQL = "SELECT * FROM Libros WHERE id_libro = " + id;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar libro");
        }
    }

    public void modificar(Integer id, String titulo, String autor, String categoria,
            Integer anioPublicacion, Boolean estadoVigencia) throws Exception {

        strSQL = "UPDATE Libros SET "
                + "titulo = '" + titulo + "', "
                + "autor_completo = '" + autor + "', "
                + "categoria = '" + categoria + "', "
                + "anio_publicacion = " + anioPublicacion + ", "
                + "estado_vigencia = " + estadoVigencia
                + " WHERE id_libro = " + id;

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar libro: " + e.getMessage());
        }
    }

    public void darBajaLibro(Integer id) throws Exception {
        // Establece el campo estado_vigencia a FALSE
        String strSQL = "UPDATE Libros SET estado_vigencia = FALSE WHERE id_libro = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja el libro");
        }
    }


    public void eliminarLibro(Integer id) throws Exception {
        String strSQL = "DELETE FROM Libros WHERE id_libro = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar libro");
        }
    }
}
