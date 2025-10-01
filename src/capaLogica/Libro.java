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

    private clsJDBC objConectar = new clsJDBC();
    private String strSQL;
    private ResultSet rs = null;

    public ResultSet listarLibros() throws Exception {
        strSQL = "SELECT id_libro, titulo, autor_completo, categoria, anio_publicacion, estado_vigencia FROM Libros";
        try {
            // Se llama a consultarBD, que ahora solo abre y ejecuta.
            rs = objConectar.consultarBD(strSQL);
            // IMPORTANTE: El ResultSet aún está activo, PERO la conexión sí fue abierta.
            return rs;
        } catch (Exception e) {
            // Si falla la consulta, intentamos desconectar, aunque ya se maneja en clsJDBC
            if (objConectar.getCon() != null) {
                objConectar.desconectar();
            }
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

    public ResultSet buscarLibro(Integer id) throws Exception {
        strSQL = "SELECT * FROM Libros WHERE id_libro = " + id;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar libro: " + e.getMessage());
        }
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

    public void modificar(Integer id, String titulo, String autor, String categoria,
            Integer anioPublicacion, Boolean estadoVigencia) throws Exception {

        String estado = estadoVigencia ? "TRUE" : "FALSE";

        strSQL = "UPDATE Libros SET "
                + "titulo = '" + titulo.replace("'", "''") + "', " // Protección de comillas
                + "autor_completo = '" + autor.replace("'", "''") + "', " // Protección de comillas
                + "categoria = '" + categoria.replace("'", "''") + "', " // Protección de comillas
                + "anio_publicacion = " + anioPublicacion + ", "
                + "estado_vigencia = " + estado
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
            throw new Exception("Error al eliminar libro: " + e.getMessage());
        }
    }

    public void modificarVigencia(Integer id, Boolean nuevoEstadoVigencia) throws Exception {
        // Convierte el booleano de Java al valor que espera la BD ('TRUE' o 'FALSE')
        String estado = nuevoEstadoVigencia ? "TRUE" : "FALSE";

        String strSQL = "UPDATE Libros SET estado_vigencia = " + estado + " WHERE id_libro = " + id;

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar la vigencia del libro: " + e.getMessage());
        }
    }
       public ResultSet buscarLibrosPorTitulo(String titulo) throws Exception {
        // Usamos LIKE y el comodín % para buscar coincidencias parciales.
        // Usamos ILIKE o LOWER para una búsqueda que no distinga mayúsculas y minúsculas (PostgreSQL).
        // Si usas MySQL/SQL Server, solo usa LIKE.
        // Asumo PostgreSQL por el "t" de booleano:
        strSQL = "SELECT * FROM Libros WHERE LOWER(titulo) LIKE '%" + titulo.toLowerCase() + "%'";
        
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar libros por título: " + e.getMessage());
        }
    }
}
