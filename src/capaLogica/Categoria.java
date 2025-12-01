/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Tocto Portocarrero Rodrigo Jesús
 */
public class Categoria {

    clsJDBC con = new clsJDBC();
    String sql = "";
    //Necesito un metodo que me liste todas mis categorias

    public ResultSet listarCategoria() throws Exception {
        try {
            sql = "SELECT * FROM CATEGORIA";
            Statement st = con.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            return rs;

        } catch (Exception e) {
            throw new Exception("Error al listar categoria: " + e.getMessage());
        }
    }

    //Buscar categoria
    public ResultSet buscarCategoria(int idcategoria) throws Exception {
        try {
            sql = "SELECT * FROM CATEGORIA where idcategoria = ?";
            PreparedStatement pst = con.conectar().prepareStatement(sql);
            pst.setInt(1, idcategoria);
            ResultSet rs = pst.executeQuery();

            return rs;

        } catch (Exception e) {
            throw new Exception("Error al buscar categoria: " + e.getMessage());
        }
    }

    //Necesito un metodo que me cree un id para categoria
    public int crearCodigoCategoria() throws Exception {
        try {
            sql = "SELECT COALESCE(MAX(idcategoria), 0) + 1 AS codigocategoria FROM categoria";
            Statement st = con.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("codigocategoria");
            }
        } catch (Exception e) {
            throw new Exception("Error al crear codigo: " + e.getMessage());
        }
        return 0;
    }

    // Método eliminarCategoria MEJORADO
    public void eliminarCategoria(int idcategoria) throws Exception {
        PreparedStatement pstCount = null;
        PreparedStatement pstLibros = null;
        PreparedStatement pstCategoria = null;
        PreparedStatement pstDelete = null;
        ResultSet rs = null;

        try {
            // 1. Contar la cantidad de libros asociados
            String sqlCount = "SELECT COUNT(*) AS cantidad FROM LIBROS WHERE idcategoria = ?";
            pstCount = con.conectar().prepareStatement(sqlCount);
            pstCount.setInt(1, idcategoria);
            rs = pstCount.executeQuery();

            int cantidad = 0;
            if (rs.next()) {
                cantidad = rs.getInt("cantidad");
            }

            if (cantidad > 0) {
                // Eliminación LÓGICA (Tiene libros asociados)

                // Desactivar libros
                String sqlLibros = "UPDATE LIBROS SET estado = false WHERE idcategoria = ?";
                pstLibros = con.conectar().prepareStatement(sqlLibros);
                pstLibros.setInt(1, idcategoria);
                pstLibros.executeUpdate();

                // Desactivar categoría
                String sqlCategoria = "UPDATE CATEGORIA SET estado = false WHERE idcategoria = ?";
                pstCategoria = con.conectar().prepareStatement(sqlCategoria);
                pstCategoria.setInt(1, idcategoria);
                pstCategoria.executeUpdate();

            } else {
                // Eliminación FÍSICA (No tiene libros asociados)
                String sqlDelete = "DELETE FROM CATEGORIA WHERE idcategoria = ?";
                pstDelete = con.conectar().prepareStatement(sqlDelete);
                pstDelete.setInt(1, idcategoria);
                pstDelete.executeUpdate();
            }

        } catch (Exception e) {
            throw new Exception("Error al eliminar categoría: " + e.getMessage());
        } finally {
            // Cerrar todos los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstCount != null) {
                    pstCount.close();
                }
                if (pstLibros != null) {
                    pstLibros.close();
                }
                if (pstCategoria != null) {
                    pstCategoria.close();
                }
                if (pstDelete != null) {
                    pstDelete.close();
                }
            } catch (SQLException ex) {
                // Log error but don't throw
            }
        }
    }

    public void ActualizarCategoria(int idcategoria, String nombrecategoria, String descripcion, Boolean vigencia) throws Exception {
        PreparedStatement pstUpdateCat = null;
        PreparedStatement pstCount = null;
        PreparedStatement pstLibros = null;
        ResultSet rs = null;

        try {
            // 1. Actualizar la CATEGORIA con los nuevos datos
            String sqlUpdateCat = "UPDATE CATEGORIA SET nombrecategoria = ?, descripcion = ?, estado = ? WHERE idcategoria=?";
            pstUpdateCat = con.conectar().prepareStatement(sqlUpdateCat);
            pstUpdateCat.setString(1, nombrecategoria);
            pstUpdateCat.setString(2, descripcion);
            pstUpdateCat.setBoolean(3, vigencia);
            pstUpdateCat.setInt(4, idcategoria);
            pstUpdateCat.executeUpdate();

            // 2. Si la nueva vigencia es FALSE, desactivar los libros asociados
            if (!vigencia) {
                // a. Contar la cantidad de libros asociados
                String sqlCount = "SELECT COUNT(*) AS cantidad FROM LIBROS WHERE idcategoria = ?";
                pstCount = con.conectar().prepareStatement(sqlCount);
                pstCount.setInt(1, idcategoria);
                rs = pstCount.executeQuery();

                int cantidad = 0;
                if (rs.next()) {
                    cantidad = rs.getInt("cantidad");
                }

                // b. Si tiene libros, desactivarlos también
                if (cantidad > 0) {
                    String sqlLibros = "UPDATE LIBROS SET estado = false WHERE idcategoria = ?";
                    pstLibros = con.conectar().prepareStatement(sqlLibros);
                    pstLibros.setInt(1, idcategoria);
                    pstLibros.executeUpdate();
                }
            }

        } catch (Exception e) {
            throw new Exception("Error al actualizar categoria: " + e.getMessage());
        } finally {
            // Cerrar todos los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstUpdateCat != null) {
                    pstUpdateCat.close();
                }
                if (pstCount != null) {
                    pstCount.close();
                }
                if (pstLibros != null) {
                    pstLibros.close();
                }
            } catch (SQLException ex) {
                // Log error but don't throw
            }
        }
    }

    // Método DarBajaCategoria MEJORADO
    public void DarBajaCategoria(int idcategoria) throws Exception {
        PreparedStatement pstVigencia = null;
        PreparedStatement pstCount = null;
        PreparedStatement pstLibros = null;
        PreparedStatement pstCategoria = null;
        ResultSet rsVigencia = null;
        ResultSet rsCount = null;

        try {
            // 1. Verificar la vigencia ACTUAL de la categoría
            String sqlVigencia = "SELECT estado FROM CATEGORIA WHERE idcategoria = ?";
            pstVigencia = con.conectar().prepareStatement(sqlVigencia);
            pstVigencia.setInt(1, idcategoria);
            rsVigencia = pstVigencia.executeQuery();

            boolean vigenciaActual = false;
            if (rsVigencia.next()) {
                vigenciaActual = rsVigencia.getBoolean("estado");
            }

            if (!vigenciaActual) {
                // La categoría ya está inactiva
                throw new Exception("La categoría ya está dada de baja");
            }

            // 2. Contar libros asociados
            String sqlCount = "SELECT COUNT(*) AS cantidad FROM LIBROS WHERE idcategoria = ?";
            pstCount = con.conectar().prepareStatement(sqlCount);
            pstCount.setInt(1, idcategoria);
            rsCount = pstCount.executeQuery();

            int cantidad = 0;
            if (rsCount.next()) {
                cantidad = rsCount.getInt("cantidad");
            }

            // 3. Si tiene libros, desactivarlos
            if (cantidad > 0) {
                String sqlLibros = "UPDATE LIBROS SET estado = false WHERE idcategoria = ?";
                pstLibros = con.conectar().prepareStatement(sqlLibros);
                pstLibros.setInt(1, idcategoria);
                pstLibros.executeUpdate();
            }

            // 4. Desactivar la categoría
            String sqlCategoria = "UPDATE CATEGORIA SET estado = FALSE WHERE idcategoria = ?";
            pstCategoria = con.conectar().prepareStatement(sqlCategoria);
            pstCategoria.setInt(1, idcategoria);
            pstCategoria.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al dar de baja categoría: " + e.getMessage());
        } finally {
            // Cerrar todos los recursos
            try {
                if (rsVigencia != null) {
                    rsVigencia.close();
                }
                if (rsCount != null) {
                    rsCount.close();
                }
                if (pstVigencia != null) {
                    pstVigencia.close();
                }
                if (pstCount != null) {
                    pstCount.close();
                }
                if (pstLibros != null) {
                    pstLibros.close();
                }
                if (pstCategoria != null) {
                    pstCategoria.close();
                }
            } catch (SQLException ex) {
                // Log error but don't throw
            }
        }
    }

    //Insertar categoria
    public void insertarCategoria(int idcategoria, String nombrecategoria, String descripcion, Boolean vigencia) throws Exception {
        try {
            // La consulta SQL ahora incluye el campo 'vigencia' (estado) como un parámetro (?)
            sql = "INSERT INTO CATEGORIA (idcategoria, nombrecategoria, descripcion, estado) VALUES (?, ?, ?, ?)";

            // Usamos PreparedStatement para prevenir inyección SQL y manejar los parámetros
            PreparedStatement pst = con.conectar().prepareStatement(sql);

            // Asignación de parámetros
            pst.setInt(1, idcategoria);
            pst.setString(2, nombrecategoria);
            pst.setString(3, descripcion);
            // ¡CORRECCIÓN! Se añade el parámetro de vigencia (estado)
            pst.setBoolean(4, vigencia);

            // Ejecutar la actualización
            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas == 0) {
                throw new Exception("No se pudo insertar la categoría, verifica los datos.");
            }

            // Cerrar el PreparedStatement
            pst.close();

        } catch (Exception e) {
            // Relanzar la excepción con un mensaje específico
            throw new Exception("Error al insertar la categoría: " + e.getMessage());
        }
    }

    public ResultSet listarCategoriasActivas() throws Exception {
        Statement st = null;
        try {
            sql = "SELECT idcategoria, nombrecategoria FROM CATEGORIA WHERE estado = TRUE ORDER BY nombrecategoria";
            st = con.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            // La desconexión y cierre de Statement se manejan donde se consume el ResultSet
            return rs;
        } catch (Exception e) {
            // Intentar cerrar el Statement si se abrió
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    /* ignorar */ }
            }
            throw new Exception("Error al listar categorías activas: " + e.getMessage());
        }
    }

    public ResultSet filtrarCategorias(String nombre, String descripcion, String estado) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            // 1. Iniciar la consulta base
            sql = "SELECT idcategoria, nombrecategoria, descripcion, estado FROM CATEGORIA WHERE 1=1";

            // 2. Usar un StringBuilder o List para manejar los parámetros y la cláusula WHERE
            StringBuilder sb = new StringBuilder(sql);
            int parameterIndex = 1;

            // 3. Crear la lista para almacenar los valores de los parámetros (?)
            java.util.List<Object> parametros = new java.util.ArrayList<>();

            // --- FILTRO POR NOMBRE ---
            if (nombre != null && !nombre.trim().isEmpty()) {
                sb.append(" AND UPPER(nombrecategoria) LIKE UPPER(?)");
                parametros.add("%" + nombre.trim() + "%");
            }

            // --- FILTRO POR DESCRIPCIÓN ---
            if (descripcion != null && !descripcion.trim().isEmpty()) {
                sb.append(" AND UPPER(descripcion) LIKE UPPER(?)");
                parametros.add("%" + descripcion.trim() + "%");
            }

            // --- FILTRO POR ESTADO ---
            if (estado != null && !estado.trim().equalsIgnoreCase("todos")) {
                sb.append(" AND estado = ?");
                boolean activo = estado.trim().equalsIgnoreCase("activo");
                parametros.add(activo); // Añade el valor booleano
            }

            // 4. Agregar ordenación para mejor visualización
            sb.append(" ORDER BY nombrecategoria");

            // 5. Preparar la sentencia
            pst = con.conectar().prepareStatement(sb.toString());

            // 6. Asignar los parámetros dinámicamente
            for (Object param : parametros) {
                if (param instanceof String) {
                    pst.setString(parameterIndex++, (String) param);
                } else if (param instanceof Boolean) {
                    pst.setBoolean(parameterIndex++, (Boolean) param);
                }
            }

            // 7. Ejecutar la consulta
            rs = pst.executeQuery();
            return rs;

        } catch (Exception e) {
            // Asegurar que el PreparedStatement se cierre en caso de error
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    /* ignored */ }
            }
            throw new Exception("Error al filtrar categorías: " + e.getMessage());
        }
    }
}
