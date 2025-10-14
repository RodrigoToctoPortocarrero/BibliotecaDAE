/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Percy Alexander
 */
public class Libro {

    private clsJDBC objConectar = new clsJDBC();
    private String strSQL;
    private ResultSet rs = null;

    /**
     * Lista los libros mostrando el nombre de la Editorial y Categoría.
     * También, para la vista previa, concatena a los autores. Los nombres de
     * las columnas se han adaptado al esquema de la BD.
     */
    public ResultSet listarLibros() throws Exception {
        strSQL = "SELECT "
                + "L.idlibro, "
                + "L.titulo, "
                + "L.aniopublicacion, "
                + "L.estado, "
                + "E.nombre AS nombre_editorial, "
                + "C.nombrecategoria AS nombre_categoria, "
                // ⬇️ CLAVE: Agregamos STRING_AGG y COALESCE para manejar libros sin autores ⬇️
                + "COALESCE(STRING_AGG(A.nombres || ' ' || A.apepaterno, ', ' ORDER BY A.apepaterno), '') AS autores_concatenados "
                + "FROM LIBROS L "
                // INNER JOIN es correcto aquí porque ideditorial e idcategoria son NOT NULL
                + "INNER JOIN EDITORIAL E ON L.ideditorial = E.ideditorial "
                + "INNER JOIN CATEGORIA C ON L.idcategoria = C.idcategoria "
                // LEFT JOIN es crucial: asegura que se listen libros que AÚN NO tienen autores
                + "LEFT JOIN ASIGNAR_LIBRO_AUTOR ALA ON L.idlibro = ALA.idlibro "
                + "LEFT JOIN AUTOR A ON ALA.idautor = A.idautor "
                // Agrupación obligatoria de todas las columnas que no usan STRING_AGG
                + "GROUP BY L.idlibro, L.titulo, L.aniopublicacion, L.estado, E.nombre, C.nombrecategoria "
                + "ORDER BY L.idlibro";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar libros: " + e.getMessage());
        }
    }

    /**
     * Genera el siguiente ID de la clave primaria (idlibro)
     */
    public Integer generarCodigoLibro() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idlibro), 0) + 1 AS codigo FROM LIBROS";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de libro: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Busca un libro por su ID.
     */
    public ResultSet buscarLibro(Integer id) throws Exception {
        strSQL = "SELECT * FROM LIBROS WHERE idlibro = " + id;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar libro: " + e.getMessage());
        }
    }

    // --- NUEVAS FUNCIONES PARA LOS SELECTORES ---
    /**
     * Devuelve las editoriales activas para un ComboBox/Selector.
     */
    public ResultSet listarEditorialesActivas() throws Exception {
        strSQL = "SELECT ideditorial, nombre FROM EDITORIAL WHERE estado = TRUE";
        try {
            return objConectar.consultarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al listar editoriales: " + e.getMessage());
        }
    }

    /**
     * Devuelve las categorías activas para un ComboBox/Selector.
     */
    public ResultSet listarCategoriasActivas() throws Exception {
        strSQL = "SELECT idcategoria, nombrecategoria FROM CATEGORIA WHERE estado = TRUE";
        try {
            return objConectar.consultarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al listar categorías: " + e.getMessage());
        }
    }

    /**
     * Devuelve los autores activos para el selector de asignación.
     */
    public ResultSet listarAutoresActivos() throws Exception {
        strSQL = "SELECT idautor, nombres, apepaterno, apematerno FROM AUTOR WHERE estado = TRUE";
        try {
            return objConectar.consultarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al listar autores: " + e.getMessage());
        }
    }

    /**
     * Devuelve los autores ya asignados a un libro específico.
     */
    public ResultSet listarAutoresPorLibro(Integer idLibro) throws Exception {
        strSQL = "SELECT A.idautor, A.nombres, A.apepaterno, ALA.descripcion "
                + "FROM AUTOR A "
                + "INNER JOIN ASIGNAR_LIBRO_AUTOR ALA ON A.idautor = ALA.idautor "
                + "WHERE ALA.idlibro = " + idLibro;
        try {
            return objConectar.consultarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al listar autores del libro: " + e.getMessage());
        }
    }

    // ---------------------------------------------
    /**
     * Registra un nuevo libro. IMPORTANTE: Los parámetros han cambiado para
     * usar las FK (ideditorial, idcategoria).
     */
    public void registrar(Integer id, String titulo, String anioPublicacion,
            Boolean estado, Integer idEditorial, Integer idCategoria) throws Exception {

        // Uso de L.estado (BOOLEAN) y L.aniopublicacion (CHAR(4)) del esquema
        String estadoDB = estado ? "TRUE" : "FALSE";

        // Se recomienda usar PreparedStatement, pero por ahora se mantiene la concatenación
        strSQL = "INSERT INTO LIBROS (idlibro, titulo, aniopublicacion, estado, ideditorial, idcategoria) VALUES ("
                + id + ", '" + titulo.replace("'", "''") + "', '" + anioPublicacion + "', "
                + estadoDB + ", " + idEditorial + ", " + idCategoria + ")";

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar libro: " + e.getMessage());
        }
    }

    public void asignarAutor(Integer idLibro, Integer idAutor, String descripcion) throws Exception {
        strSQL = "INSERT INTO ASIGNAR_LIBRO_AUTOR (idlibro, idautor, descripcion) VALUES ("
                + idLibro + ", " + idAutor + ", '" + descripcion.replace("'", "''") + "')";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            // Manejo de error más descriptivo en caso de violación de clave primaria (autor ya asignado)
            String errorMessage = e.getMessage().toLowerCase();

            if (errorMessage.contains("duplicate key") || errorMessage.contains("violates unique constraint")) {
                throw new Exception("Error: Este autor ya está asignado al libro. No se permiten autores duplicados.");
            } else {
                throw new Exception("Error al asignar autor al libro: " + e.getMessage());
            }
        }
    }

    public void removerAutor(Integer idLibro, Integer idAutor) throws Exception {
        String strSQL = "DELETE FROM ASIGNAR_LIBRO_AUTOR WHERE idlibro = " + idLibro + " AND idautor = " + idAutor;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al remover autor del libro: " + e.getMessage());
        }
    }

    public boolean esAutorAsignado(Integer idLibro, Integer idAutor) throws Exception {
        ResultSet rsCheck = null;
        try {
            // Consulta que verifica si existe el par (idLibro, idAutor) en la tabla de relación
            String checkSQL = "SELECT 1 FROM ASIGNAR_LIBRO_AUTOR WHERE idlibro = " + idLibro + " AND idautor = " + idAutor;

            // objConectar.consultarBD(sql) abre, ejecuta y devuelve el ResultSet.
            rsCheck = objConectar.consultarBD(checkSQL);

            // rsCheck.next() devuelve true si encontró una fila, indicando que ya está asignado.
            return rsCheck.next();
        } catch (Exception e) {
            throw new Exception("Error al verificar asignación en BD: " + e.getMessage());
        } finally {
            // Es vital cerrar el ResultSet
            if (rsCheck != null) {
                try {
                    rsCheck.close();
                } catch (SQLException ex) {
                    /* Ignorar */ }
            }
        }
    }

    /**
     * Modifica los datos principales del libro. IMPORTANTE: Los parámetros han
     * cambiado para usar las FK (ideditorial, idcategoria).
     */
    public void modificar(Integer id, String titulo, String anioPublicacion,
            Boolean estado, Integer idEditorial, Integer idCategoria) throws Exception {

        String estadoDB = estado ? "TRUE" : "FALSE";

        strSQL = "UPDATE LIBROS SET "
                + "titulo = '" + titulo.replace("'", "''") + "', "
                + "aniopublicacion = '" + anioPublicacion + "', "
                + "ideditorial = " + idEditorial + ", "
                + "idcategoria = " + idCategoria + ", "
                + "estado = " + estadoDB
                + " WHERE idlibro = " + id;

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar libro: " + e.getMessage());
        }
    }

    /**
     * Establece el campo estado a FALSE.
     */
    public void darBajaLibro(Integer id) throws Exception {
        // Se asume que el campo para la vigencia es 'estado' (tipo boolean)
        String strSQL = "UPDATE LIBROS SET estado = FALSE WHERE idlibro = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja el libro: " + e.getMessage());
        }
    }

    /**
     * Elimina el libro (y se confía en la BD para eliminar las referencias en
     * ASIGNAR_LIBRO_AUTOR si se ha configurado la restricción ON DELETE
     * CASCADE, sino fallará y es correcto).
     */
    public void eliminarLibro(Integer id) throws Exception {
        String strSQL = "DELETE FROM LIBROS WHERE idlibro = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            // Este error puede ocurrir si tiene ejemplares o autores asignados y no hay CASCADE.
            throw new Exception("Error al eliminar libro. Verifique que no tenga ejemplares o autores asociados: " + e.getMessage());
        }
    }

    /**
     * Modifica el estado de vigencia (Activo/Inactivo).
     */
    public void modificarVigencia(Integer id, Boolean nuevoEstado) throws Exception {
        String estadoDB = nuevoEstado ? "TRUE" : "FALSE";
        String strSQL = "UPDATE LIBROS SET estado = " + estadoDB + " WHERE idlibro = " + id;

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar la vigencia del libro: " + e.getMessage());
        }
    }

    /**
     * Busca libros por coincidencia en el título.
     */
    public ResultSet buscarLibrosPorTitulo(String titulo) throws Exception {
        // Se usa LOWER para búsqueda insensible a mayúsculas/minúsculas y LIKE para coincidencias parciales.
        strSQL = "SELECT * FROM LIBROS WHERE LOWER(titulo) LIKE '%" + titulo.toLowerCase().replace("'", "''") + "%'";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar libros por título: " + e.getMessage());
        }
    }
}
