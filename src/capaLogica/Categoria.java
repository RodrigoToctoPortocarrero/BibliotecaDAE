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

    //Necesito un metodo que me elimine
    public void eliminarCategoria(int idcategoria) throws Exception {
        try {
            // 1. Contar la cantidad de libros asociados
            String sqlCount = "SELECT COUNT(*) AS cantidad FROM LIBROS WHERE idcategoria = ?";
            PreparedStatement pstCount = con.conectar().prepareStatement(sqlCount);
            pstCount.setInt(1, idcategoria);
            ResultSet rs = pstCount.executeQuery();

            int cantidad = 0;
            // ¡CORRECCIÓN CRÍTICA! Es necesario mover el cursor con rs.next() para leer los datos
            if (rs.next()) {
                cantidad = rs.getInt("cantidad");
            }

            // Cierre de recursos del conteo
            rs.close();
            pstCount.close();

            if (cantidad > 0) {
                // Eliminación LÓGICA (Tiene libros asociados)

                // Desactivar libros
                String sqlLibros = "UPDATE LIBROS SET estado = false WHERE idcategoria = ?";
                PreparedStatement pstLibros = con.conectar().prepareStatement(sqlLibros);
                pstLibros.setInt(1, idcategoria);
                pstLibros.executeUpdate();
                pstLibros.close();

                // Desactivar categoría
                String sqlCategoria = "UPDATE CATEGORIA SET estado = false WHERE idcategoria = ?";
                PreparedStatement pstCategoria = con.conectar().prepareStatement(sqlCategoria);
                pstCategoria.setInt(1, idcategoria);
                pstCategoria.executeUpdate();
                pstCategoria.close();

            } else {
                // Eliminación FÍSICA (No tiene libros asociados)
                String sqlDelete = "DELETE FROM CATEGORIA WHERE idcategoria = ?";
                PreparedStatement pstDelete = con.conectar().prepareStatement(sqlDelete);
                pstDelete.setInt(1, idcategoria);
                pstDelete.executeUpdate();
                pstDelete.close();
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar categoría: " + e.getMessage());
        }
    }

    //Actualizar categoria
    public void ActualizarCategoria(int idcategoria, String nombrecategoria, String descripcion, Boolean vigencia) throws Exception {
        try {
            // 1. Actualizar la CATEGORIA con los nuevos datos (incluyendo la vigencia)
            String sqlUpdateCat = "UPDATE CATEGORIA SET nombrecategoria = ?, descripcion = ?, estado = ? WHERE idcategoria=?";
            PreparedStatement pstUpdateCat = con.conectar().prepareStatement(sqlUpdateCat);
            pstUpdateCat.setString(1, nombrecategoria);
            pstUpdateCat.setString(2, descripcion);
            pstUpdateCat.setBoolean(3, vigencia);
            pstUpdateCat.setInt(4, idcategoria);
            pstUpdateCat.executeUpdate();
            pstUpdateCat.close();

            // 2. Si la nueva vigencia es FALSE, revisar si tiene libros y desactivarlos
            if (!vigencia) {

                // a. Contar la cantidad de libros asociados
                String sqlCount = "SELECT COUNT(*) AS cantidad FROM LIBROS WHERE idcategoria = ?";
                PreparedStatement pstCount = con.conectar().prepareStatement(sqlCount);
                pstCount.setInt(1, idcategoria);
                ResultSet rs = pstCount.executeQuery();

                int cantidad = 0;
                // ¡CORRECCIÓN CRÍTICA! Mover el cursor para leer
                if (rs.next()) {
                    cantidad = rs.getInt("cantidad");
                }
                rs.close();
                pstCount.close();

                // b. Si tiene libros, desactivarlos también
                if (cantidad > 0) {
                    String sqlLibros = "UPDATE LIBROS SET estado = false WHERE idcategoria = ?";
                    PreparedStatement pstLibros = con.conectar().prepareStatement(sqlLibros);
                    pstLibros.setInt(1, idcategoria);
                    pstLibros.executeUpdate();
                    pstLibros.close();
                }
            }
            // Nota: Si la vigencia es TRUE, se asume que los libros asociados (si los hay)
            // se manejan por separado o se actualizan a TRUE de manera explícita si el negocio lo requiere.
            // Aquí solo se sincroniza la desactivación.
            //NOTA DE RODRIGO: TIENES RAZÓN MI ESTIMADO

        } catch (Exception e) {
            throw new Exception("Error al actualizar categoria: " + e.getMessage());
        }
    }

    //Dar de baja la categoria
    public void DarBajaCategoria(int idcategoria) throws Exception {
        try {
            // 1. Encontrar la vigencia ACTUAL de la categoría
            String sqlVigencia = "SELECT estado FROM CATEGORIA WHERE idcategoria = ?";
            PreparedStatement pstVigencia = con.conectar().prepareStatement(sqlVigencia);
            pstVigencia.setInt(1, idcategoria);
            ResultSet rsVigencia = pstVigencia.executeQuery();

            boolean vigenciaActual = false;
            // ¡CORRECCIÓN CRÍTICA! Mover el cursor para leer
            if (rsVigencia.next()) {
                vigenciaActual = rsVigencia.getBoolean("estado");
            }
            rsVigencia.close();
            pstVigencia.close();

            if (vigenciaActual) {
                // La categoría está activa, procedemos a desactivarla y a los libros

                // a. Contar libros (Para determinar si se debe actualizar LIBROS)
                String sqlCount = "SELECT COUNT(*) AS cantidad FROM LIBROS WHERE idcategoria = ?";
                PreparedStatement pstCount = con.conectar().prepareStatement(sqlCount);
                pstCount.setInt(1, idcategoria);
                ResultSet rsCount = pstCount.executeQuery();

                int cantidad = 0;
                // ¡CORRECCIÓN CRÍTICA! Mover el cursor para leer
                if (rsCount.next()) {
                    cantidad = rsCount.getInt("cantidad");
                }
                rsCount.close();
                pstCount.close();

                // b. Si tiene libros, desactivarlos también (Asumo que la tabla es LIBROS, como en otros métodos)
                if (cantidad > 0) {
                    String sqlLibros = "UPDATE LIBROS SET estado = false WHERE idcategoria = ?";
                    PreparedStatement pstLibros = con.conectar().prepareStatement(sqlLibros);
                    pstLibros.setInt(1, idcategoria);
                    pstLibros.executeUpdate();
                    pstLibros.close();
                }

                // c. Desactivar la categoría (Esto siempre ocurre si vigenciaActual era TRUE)
                String sqlCategoria = "UPDATE CATEGORIA SET estado = FALSE WHERE idcategoria= ?";
                PreparedStatement pstCategoria = con.conectar().prepareStatement(sqlCategoria);
                pstCategoria.setInt(1, idcategoria);
                pstCategoria.executeUpdate();
                pstCategoria.close();

            } else {
                // La categoría ya está inactiva
                System.out.println("La vigencia de la categoría ya fue dada de baja");
            }

        } catch (Exception e) {
            throw new Exception("Error al dar de baja categoría: " + e.getMessage());
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
