package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;

/**
 * Mantenimiento de autores
 *
 * @author Edgar Bernabe (Modificado por Asistente para incluir el campo
 * 'estado')
 */
public class Autor {

    private clsJDBC objConectar = new clsJDBC();
    private String strSQL;
    private ResultSet rs = null;

    // Listar todos los autores
    public ResultSet listarAutores() throws Exception {
        strSQL = "SELECT idautor, nombres, apepaterno, apematerno, descripcion, estado FROM AUTOR";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            if (objConectar.getCon() != null) {
                objConectar.desconectar();
            }
            throw new Exception("Error al listar autores: " + e.getMessage());
        }
    }

    // Buscar autor por ID
    public ResultSet buscarAutor(Integer id) throws Exception {
        // CORRECTO
        strSQL = "SELECT * FROM AUTOR WHERE idautor = " + id;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar autor: " + e.getMessage());
        }
    }

    // ⭐ MODIFICADO: Ahora acepta el parámetro 'estado' (boolean) para la vigencia.
    public void registrar(Integer idautor, String nombres, String apepaterno, String apematerno, String descripcion, boolean estado) throws Exception {
        // Se incluye 'estado' en la inserción, recibiendo el valor del formulario
        strSQL = "INSERT INTO AUTOR (idautor, nombres, apepaterno, apematerno, descripcion, estado) VALUES ("
                + idautor + ", '" // <-- SE AÑADE EL ID
                + nombres.replace("'", "''") + "', '"
                + apepaterno.replace("'", "''") + "', '"
                + apematerno.replace("'", "''") + "', '"
                + descripcion.replace("'", "''") + "', "
                + estado + ")";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar autor: " + e.getMessage());
        }
    }

    // Modificar autor
    // CORRECTO: Ya manejaba el estado (Boolean estado)
    public void modificar(Integer id, String nombres, String apepaterno, String apematerno, String descripcion, Boolean estado) throws Exception {
        strSQL = "UPDATE AUTOR SET "
                + "nombres = '" + nombres.replace("'", "''") + "', "
                + "apepaterno = '" + apepaterno.replace("'", "''") + "', "
                + "apematerno = '" + apematerno.replace("'", "''") + "', "
                + "descripcion = '" + descripcion.replace("'", "''") + "', "
                + "estado = " + estado + " " // Permite cambiar el estado
                + "WHERE idautor = " + id;

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar autor: " + e.getMessage());
        }
    }

    // ⭐ MODIFICADO: Cambiado de DELETE a Eliminación Lógica (UPDATE estado = FALSE)
    public void eliminarAutor(Integer id) throws Exception {
        // La eliminación lógica es preferible a borrar datos.
        // Solo cambiamos el estado a FALSE/0 (Inactivo)
        strSQL = "UPDATE AUTOR SET estado = FALSE WHERE idautor = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            // Si quieres volver a la eliminación física, cambia la línea de strSQL al DELETE original
            throw new Exception("Error al eliminar (cambiar estado a Inactivo) autor: " + e.getMessage());
        }
    }

    // (Opcional) Obtener nuevo ID
    public Integer generarCodigoAutor() throws Exception {
        // CORRECTO
        strSQL = "SELECT COALESCE(MAX(idautor), 0) + 1 AS codigo FROM AUTOR";
        try {
            rs = objConectar.consultarBD(strSQL);
            while (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de autor");
        }
        return 0;
    }

    public ResultSet listarAutoresActivos() throws Exception {
        strSQL = "SELECT idautor, nombres, apepaterno, apematerno FROM AUTOR WHERE estado = TRUE ORDER BY apepaterno, nombres";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            if (objConectar.getCon() != null) {
                objConectar.desconectar();
            }
            throw new Exception("Error al listar autores activos: " + e.getMessage());
        }
    }

    public void asignarAutor(Integer idLibro, Integer idAutor, String descripcion) throws Exception {
        String strSQL = "INSERT INTO ASIGNAR_LIBRO_AUTOR (idlibro, idautor, descripcion) VALUES ("
                + idLibro + ", " + idAutor + ", '" + descripcion.replace("'", "''") + "')";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            // Puede fallar si el par (idLibro, idAutor) ya existe (violación de clave primaria)
            throw new Exception("Error al asignar autor al libro. Verifique que el autor no esté ya asignado. Mensaje: " + e.getMessage());
        }
    }

    public ResultSet filtrarAutores(String nombres, String estado) throws Exception {

        try {

            // 1. Consulta Base (Selecciona todos los campos)
            StringBuilder sb = new StringBuilder(
                    "SELECT idautor, nombres, apepaterno, apematerno, descripcion, estado FROM AUTOR WHERE 1=1"
            );

            // 2. Filtro por NOMBRES
            if (nombres != null && !nombres.trim().isEmpty()) {

                // Usamos concatenación directa para el LIKE, sanitizando la entrada
                sb.append(" AND UPPER(nombres) LIKE UPPER('%")
                        .append(nombres.trim().replace("'", "''"))
                        .append("%')");

            }

            // 4. Filtro por ESTADO
            if (estado != null && !estado.trim().equalsIgnoreCase("Todos")) {

                // Convierte la cadena "Activo" o "Inactivo" a valor booleano o cadena SQL
                String valorEstado = estado.trim().equalsIgnoreCase("Activo") ? "TRUE" : "FALSE";

                sb.append(" AND estado = ")
                        .append(valorEstado);

            }

            // 5. Agregar ordenación
            sb.append(" ORDER BY nombres");

            // 6. Asignar la SQL final a la variable de instancia y ejecutar
            strSQL = sb.toString();

            rs = objConectar.consultarBD(strSQL);

            return rs;

        } catch (Exception e) {

            // Asegurar que la conexión se desconecte si falló la consulta (patrón de tu clase)
            if (objConectar.getCon() != null) {

                objConectar.desconectar();

            }

            throw new Exception("Error al filtrar autores: " + e.getMessage());

        }

    }

    // Método para verificar si el autor está presente en la tabla ASIGNAR_LIBRO_AUTOR
    public boolean tieneLibrosAsignados(Integer idAutor) throws Exception {
        strSQL = "SELECT COUNT(*) AS total FROM ASIGNAR_LIBRO_AUTOR WHERE idautor = " + idAutor;
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al verificar asignación de libros: " + e.getMessage());
        }
    }

    public void eliminarFisico(Integer id) throws Exception {
        strSQL = "DELETE FROM AUTOR WHERE idautor = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar físicamente al autor: " + e.getMessage());
        }
    }
}
