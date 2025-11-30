package capaLogica;

import capaDatos.clsJDBC;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Valentino Lopez
 */
public class Editorial {

    private clsJDBC objJDBC = new clsJDBC(); // Instancia de la clase de conexión

    // Propiedades de la Editorial (Modelo de datos)
    private int ideditorial;
    private String nombre;
    private String telefono;
    private String correo;
    private boolean estado;

    // Constructores y Getters/Setters (omitiendo por brevedad, asume que existen)
    public Editorial() {
    }

    public int getIdeditorial() {
        return ideditorial;
    }

    public void setIdeditorial(int ideditorial) {
        this.ideditorial = ideditorial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // ------------------------------------
    // LÓGICA DE ACCESO A DATOS
    // ------------------------------------
    // Listar Editorial
    public ResultSet listarEditorial() throws Exception {
        Connection con = null;
        try {
            con = objJDBC.conectar();
            String sql = "SELECT ideditorial, nombre, telefono, correo, estado FROM EDITORIAL ORDER BY ideditorial";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs; // La desconexión debe hacerse después de leer el ResultSet
        } catch (Exception e) {
            // Manejo de la conexión en caso de error (similar a tu Categoria.java)
            if (con != null) {
                objJDBC.desconectar();
            }
            throw new Exception("Error al listar editoriales: " + e.getMessage());
        }
    }

    // Buscar Editorial
    public ResultSet buscarPorCodigo(int ideditorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "SELECT ideditorial, nombre, telefono, correo, estado FROM EDITORIAL WHERE ideditorial = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, ideditorial);

            return pst.executeQuery(); // La desconexión debe hacerse después de leer el ResultSet
        } catch (Exception e) {
            if (con != null) {
                objJDBC.desconectar();
            }
            throw new Exception("Error al buscar editorial: " + e.getMessage());
        }
    }

    public void registrar(Editorial editorial) throws Exception {
        // 1. Definimos la SQL con placeholders (?) para todas las columnas
        String sql = "INSERT INTO EDITORIAL (ideditorial, nombre, telefono, correo, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pst = null;

        try {
            // Asumo que 'objJDBC' es tu instancia de la clase de conexión/utilidad
            con = objJDBC.conectar();
            pst = con.prepareStatement(sql);

            // 2. Asignamos los valores a los placeholders
            pst.setInt(1, editorial.getIdeditorial()); // CLAVE PRIMARIA
            pst.setString(2, editorial.getNombre());
            pst.setString(3, editorial.getTelefono());
            pst.setString(4, editorial.getCorreo());
            pst.setBoolean(5, editorial.isEstado());

            // 3. Ejecutamos la inserción
            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas == 0) {
                throw new Exception("No se insertó la Editorial. Verifique que el ID no exista y que la conexión sea válida.");
            }

        } catch (SQLException e) {
            // Esto es lo que captura el error de clave duplicada o de valor NULL
            throw new Exception("Error de base de datos al registrar editorial: " + e.getMessage());
        } catch (Exception e) {
            // Capturamos cualquier otro error
            throw new Exception("Error al registrar editorial: " + e.getMessage());
        } finally {
            // 4. Cerramos recursos
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                objJDBC.desconectar();
            }
        }
    }

    // Modificar Editorial (Actualizar)
    public void modificar(Editorial editorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "UPDATE EDITORIAL SET nombre=?, telefono=?, correo=?, estado=? WHERE ideditorial=?";

            pst = con.prepareStatement(sql);

            pst.setString(1, editorial.getNombre());
            pst.setString(2, editorial.getTelefono());
            pst.setString(3, editorial.getCorreo());
            pst.setBoolean(4, editorial.isEstado());
            pst.setInt(5, editorial.getIdeditorial()); // Condición WHERE

            pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al modificar editorial: " + e.getMessage());
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                objJDBC.desconectar(); // Cierre de recursos
            }
        }
    }

    // Dar Baja Editorial (Cambiar estado a FALSE)
    public void darBaja(int ideditorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "UPDATE EDITORIAL SET estado=FALSE WHERE ideditorial= ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, ideditorial);
            pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al dar de baja editorial: " + e.getMessage());
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                objJDBC.desconectar();
            }
        }
    }

    // Eliminar Editorial (Físicamente)
    public void eliminar(int ideditorial) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = objJDBC.conectar();
            String sql = "DELETE FROM EDITORIAL WHERE ideditorial = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, ideditorial);
            pst.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al eliminar editorial: " + e.getMessage());
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                objJDBC.desconectar();
            }
        }
    }

    public ResultSet listarEditorialesActivas() throws Exception {
        Connection con = null;
        Statement st = null;
        try {
            con = objJDBC.conectar();
            String sql = "SELECT ideditorial, nombre FROM EDITORIAL WHERE estado = TRUE ORDER BY nombre";
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            // Cierra la conexión si hay error, ya que el ResultSet no se generó.
            if (con != null) {
                objJDBC.desconectar();
            }
            // Intenta cerrar el Statement si se abrió
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    /* ignorar */ }
            }
            throw new Exception("Error al listar editoriales activas: " + e.getMessage());
        }
    }

    public ResultSet filtrarEditoriales(String nombre, String telefono, String estado) throws Exception {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = objJDBC.conectar();

            // 1. Iniciar la consulta base
            // Nota: Asegúrate de seleccionar el campo 'correo' si todavía lo necesitas para el JTable, 
            // aunque no se use para filtrar. Aquí seleccionaremos todos los campos que tiene la tabla.
            StringBuilder sb = new StringBuilder("SELECT ideditorial, nombre, telefono, correo, estado FROM EDITORIAL WHERE 1=1");

            java.util.List<Object> parametros = new java.util.ArrayList<>();

            // --- FILTRO POR NOMBRE (Búsqueda parcial, case insensitive) ---
            if (nombre != null && !nombre.trim().isEmpty()) {
                sb.append(" AND UPPER(nombre) LIKE UPPER(?)");
                parametros.add("%" + nombre.trim() + "%");
            }

            // --- FILTRO POR TELÉFONO (Búsqueda parcial, solo dígitos si es necesario) ---
            if (telefono != null && !telefono.trim().isEmpty()) {
                sb.append(" AND telefono LIKE ?");
                parametros.add("%" + telefono.trim() + "%");
            }

            // --- FILTRO POR ESTADO (Booleano) ---
            if (estado != null && !estado.trim().equalsIgnoreCase("Todos")) {
                sb.append(" AND estado = ?");
                boolean activo = estado.trim().equalsIgnoreCase("Activo");
                parametros.add(activo); // Añade el valor booleano
            }

            sb.append(" ORDER BY nombre");

            // 2. Preparar la sentencia
            pst = con.prepareStatement(sb.toString());

            // 3. Asignar los parámetros dinámicamente
            int index = 1;
            for (Object param : parametros) {
                if (param instanceof String) {
                    pst.setString(index++, (String) param);
                } else if (param instanceof Boolean) {
                    pst.setBoolean(index++, (Boolean) param);
                }
            }

            // 4. Ejecutar la consulta
            rs = pst.executeQuery();
            return rs;

        } catch (Exception e) {
            // Cierre de conexión y recursos en caso de error
            if (con != null) {
                objJDBC.desconectar();
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    /* ignored */ }
            }
            throw new Exception("Error al filtrar editoriales: " + e.getMessage());
        }
    }
public boolean tieneLibrosActivos(int idEditorial) {
    String sql = "SELECT COUNT(*) AS total FROM LIBROS WHERE ideditorial = " + idEditorial + " AND estado = TRUE";

    try {
        // --- AGREGA ESTA LÍNEA AQUÍ PARA CREAR LA CONEXIÓN ---
        capaDatos.clsJDBC objCon = new capaDatos.clsJDBC(); 
        // -----------------------------------------------------

        ResultSet rs = objCon.consultarBD(sql);

        if (rs.next()) {
            int cantidad = rs.getInt("total");
            // Importante: desconectar después de usar para liberar recursos
            objCon.desconectar(); 
            return cantidad > 0; 
        }
        objCon.desconectar(); // Desconectar si no entró al if

    } catch (Exception e) {
        System.out.println("Error al verificar libros activos: " + e.getMessage());
    }
    return false;
}

    public int generarSiguienteId() {
        int siguienteId = 1; // Valor por defecto si la tabla está vacía
        
        // Consulta para obtener el máximo ID y sumarle 1
        String sql = "SELECT COALESCE(MAX(ideditorial), 0) + 1 FROM EDITORIAL";
        
        try {
            // Instanciamos la conexión
            capaDatos.clsJDBC objCon = new capaDatos.clsJDBC();
            java.sql.ResultSet rs = objCon.consultarBD(sql);
            
            if (rs.next()) {
                siguienteId = rs.getInt(1);
            }
            objCon.desconectar();
            
        } catch (Exception e) {
            System.out.println("Error al generar ID: " + e.getMessage());
        }
        
        return siguienteId;
    }
}
