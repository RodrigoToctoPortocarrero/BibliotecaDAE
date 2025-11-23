package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;

public class Ejemplar {

    clsJDBC con = new clsJDBC();
    String sql = "";

    // ============================
    // GENERAR ID INCREMENTAL
    // ============================
    public int generarIdEjemplar() throws Exception {
        try {
            sql = "SELECT COALESCE(MAX(idejemplar),0) + 1 AS codigo FROM ejemplar";
            Statement st = con.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("codigo");
            }

        } catch (Exception e) {
            throw new Exception("Error al generar id: " + e.getMessage());
        }
        return -1;
    }

    // ============================
    // GENERAR PREFIJO DEL TÍTULO (opcional)
    // ============================
    private String generarPrefijo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) return "X";
        String[] palabras = titulo.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String p : palabras) {
            if (!p.isEmpty()) sb.append(Character.toUpperCase(p.charAt(0)));
        }
        return sb.toString();
    }

    // ============================
    // GENERAR CÓDIGO DE EJEMPLAR (opcional)
    // ============================
    public String generarCodigoEjemplar(String tituloLibro) throws Exception {
        String sqlLibro = "SELECT idlibro FROM libros WHERE titulo = ?";
        String sqlCount = "SELECT COUNT(*) AS total FROM ejemplar WHERE idlibro = ?";

        try {
            Connection cn = con.conectar();

            // Obtener idlibro
            PreparedStatement pstLibro = cn.prepareStatement(sqlLibro);
            pstLibro.setString(1, tituloLibro);
            ResultSet rsLibro = pstLibro.executeQuery();

            int idLibro;
            if (rsLibro.next()) {
                idLibro = rsLibro.getInt("idlibro");
            } else {
                throw new Exception("No existe un libro con el título: " + tituloLibro);
            }

            // Contar ejemplares del libro
            PreparedStatement pstCount = cn.prepareStatement(sqlCount);
            pstCount.setInt(1, idLibro);
            ResultSet rsCount = pstCount.executeQuery();

            int cantidad = 0;
            if (rsCount.next()) {
                cantidad = rsCount.getInt("total");
            }

            int numeroEjemplar = cantidad + 1;

            // Generar prefijo
            String prefijo = generarPrefijo(tituloLibro);

            return prefijo + "-" + numeroEjemplar;

        } catch (Exception e) {
            throw new Exception("Error al generar código de ejemplar: " + e.getMessage());
        }
    }

    // ============================
    // LISTAR TODOS LOS EJEMPLARES (con título)
    // ============================
    public ResultSet listarEjemplares() throws Exception {
        try {
            sql = "SELECT ej.idejemplar, ej.nroejemplar, ej.estado, li.titulo "
                + "FROM ejemplar ej "
                + "INNER JOIN libros li ON ej.idlibro = li.idlibro "
                + "ORDER BY ej.idejemplar";
            Statement st = con.conectar().createStatement();
            return st.executeQuery(sql);

        } catch (Exception e) {
            throw new Exception("Error al listar ejemplares: " + e.getMessage());
        }
    }

    // ============================
    // LISTAR TITULOS DE LIBROS (para cbo)
    // ============================
    public ResultSet listarTitulosLibros() throws Exception {
        try {
            sql = "SELECT idlibro, titulo FROM libros ORDER BY titulo";
            Statement st = con.conectar().createStatement();
            return st.executeQuery(sql);
        } catch (Exception e) {
            throw new Exception("Error al listar títulos de libros: " + e.getMessage());
        }
    }

    // ============================
    // BUSCAR ID LIBRO POR TITULO
    // ============================
    public int buscarIdLibroPorTitulo(String titulo) throws Exception {
        try {
            sql = "SELECT idlibro FROM libros WHERE titulo = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, titulo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("idlibro");
            } else {
                return -1;
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar id de libro: " + e.getMessage());
        }
    }

    // ============================
    // BUSCAR EJEMPLAR POR NRO (ILIKE)
    // ============================
    public ResultSet buscarEjemplarNroEjemplar(String nroejemplar) throws Exception {
        try {
            sql = "SELECT ej.idejemplar, ej.nroejemplar, ej.estado, li.titulo "
                + "FROM ejemplar ej "
                + "INNER JOIN libros li ON ej.idlibro = li.idlibro "
                + "WHERE ej.nroejemplar ILIKE ? "
                + "ORDER BY ej.idejemplar";

            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, "%" + nroejemplar + "%");
            return pst.executeQuery();

        } catch (Exception e) {
            throw new Exception("Error al buscar ejemplar: " + e.getMessage());
        }
    }

    // ============================
    // VERIFICAR SI EJEMPLAR TIENE PRÉSTAMO ACTIVO
    // ============================
    public boolean ejemplarTienePrestamo(int idEjemplar) throws Exception {
        String sqlCheck = "SELECT COUNT(*) AS total FROM prestamo WHERE idejemplar = ?";

        try {
            PreparedStatement pst = con.conectar().prepareStatement(sqlCheck);
            pst.setInt(1, idEjemplar);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            return false;

        } catch (Exception e) {
            throw new Exception("Error al verificar préstamos: " + e.getMessage());
        }
    }

    // ============================
    // INSERTAR EJEMPLAR
    // ============================
    public int insertarEjemplar(int idlibro, String nroejemplar, boolean estado) throws Exception {
        try {
            int idejemplar = generarIdEjemplar();

            sql = "INSERT INTO ejemplar(idejemplar, nroejemplar, estado, idlibro) "
                + "VALUES (?, ?, ?, ?)";

            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setInt(1, idejemplar);
            pst.setString(2, nroejemplar);
            pst.setBoolean(3, estado);
            pst.setInt(4, idlibro);

            return pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al insertar ejemplar: " + e.getMessage());
        }
    }

    // ============================
    // MODIFICAR EJEMPLAR (CON VALIDACIÓN)
    // ============================
    public int modificarEjemplar(int idEjemplar, int idlibro, String nroejemplar, boolean estado) throws Exception {
        try {
            // VALIDAR QUE NO TENGA PRÉSTAMOS
            if (ejemplarTienePrestamo(idEjemplar)) {
                throw new Exception("No se puede modificar. El ejemplar está asociado a uno o más préstamos.");
            }

            sql = "UPDATE ejemplar SET nroejemplar = ?, estado = ?, idlibro = ? WHERE idejemplar = ?";

            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setString(1, nroejemplar);
            pst.setBoolean(2, estado);
            pst.setInt(3, idlibro);
            pst.setInt(4, idEjemplar);

            return pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al modificar ejemplar: " + e.getMessage());
        }
    }

    // ============================
    // DAR DE BAJA (estado = false) CON VALIDACIÓN
    // ============================
    public int darBajaEjemplar(int idEjemplar) throws Exception {
        try {
            // VALIDAR QUE NO TENGA PRÉSTAMOS
            if (ejemplarTienePrestamo(idEjemplar)) {
                throw new Exception("No se puede dar de baja. El ejemplar está asociado a uno o más préstamos.");
            }

            sql = "UPDATE ejemplar SET estado = false WHERE idejemplar = ?";

            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setInt(1, idEjemplar);

            return pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al dar de baja ejemplar: " + e.getMessage());
        }
    }

    // ============================
    // ELIMINAR EJEMPLAR (FÍSICO) CON VALIDACIÓN
    // ============================
    public int eliminarEjemplar(int idEjemplar) throws Exception {
        try {
            // VALIDAR QUE NO TENGA PRÉSTAMOS
            if (ejemplarTienePrestamo(idEjemplar)) {
                throw new Exception("No se puede eliminar. El ejemplar está asociado a uno o más préstamos.");
            }

            sql = "DELETE FROM ejemplar WHERE idejemplar = ?";

            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setInt(1, idEjemplar);

            return pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al eliminar ejemplar: " + e.getMessage());
        }
    }

    // ============================
    // BUSCAR EJEMPLAR POR ID
    // ============================
    public ResultSet buscarEjemplar(int idEjemplar) throws Exception {
        try {
            sql = "SELECT ej.idejemplar, ej.nroejemplar, ej.estado, li.titulo "
                + "FROM ejemplar ej "
                + "INNER JOIN libros li ON ej.idlibro = li.idlibro "
                + "WHERE ej.idejemplar = ?";

            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setInt(1, idEjemplar);

            return pst.executeQuery();

        } catch (Exception e) {
            throw new Exception("Error al buscar ejemplar: " + e.getMessage());
        }
    }
}