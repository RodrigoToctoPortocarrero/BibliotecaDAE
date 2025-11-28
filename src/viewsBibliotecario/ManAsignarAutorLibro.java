/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewsBibliotecario;

import capaLogica.Autor;
import capaLogica.Libro;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.logging.Level; // 🚨 NECESARIO PARA COMPILAR LOGGER
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Percy Alexander
 */
public class ManAsignarAutorLibro extends javax.swing.JPanel {

    private HashMap<String, Integer> mapAutoresSeleccionados = new HashMap<>();
    private HashMap<String, Integer> mapLibros = new HashMap<>();
    private Integer idLibroSeleccionado; // ⬅️ Variable para guardar el ID del libro
    private Autor objAutor = new Autor();
    private Libro objLibro = new Libro();

    /**
     * Creates new form asignarautor
     */
    public ManAsignarAutorLibro() {
        initComponents();
        cargarAutoresActivos();
        cargarLibrosActivos();
        cargarInformeAsignaciones();

    }

    private void cargarAutoresActivos() {
        try {
            // objAutor debe tener el método listarAutoresActivos()
            ResultSet rs = objAutor.listarAutoresActivos();
            DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();

            modelo.addElement("-- Seleccione Autor --");
            mapAutoresSeleccionados.clear();

            while (rs.next()) {
                Integer id = rs.getInt("idautor");
                String nombres = rs.getString("nombres");
                String apePaterno = rs.getString("apepaterno");
                String apeMaterno = rs.getString("apematerno");

                String nombreCompleto = (nombres + " " + apePaterno + " " + apeMaterno).trim();

                modelo.addElement(nombreCompleto);
                mapAutoresSeleccionados.put(nombreCompleto, id);
            }

            cmbAutores.setModel(modelo); // Asigna el modelo al ComboBox
            rs.close();

        } catch (Exception e) {
            // 🚨 Muestra la excepción en caso de que falle la BD
            e.printStackTrace(); // Imprime el error completo en consola
            JOptionPane.showMessageDialog(this, "Error al cargar Autores: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarLibrosActivos() {
        // NOTA: Si el cboLibros solo debe mostrar UN libro seleccionado, esta función 
        // podría no ser necesaria, pero la mantendremos para cargar el combo con 
        // todos los libros activos si es un combo de búsqueda general.
        try {
            // objLibro.listarLibros() trae todos los libros (activos e inactivos)
            ResultSet rs = objLibro.listarLibros();
            DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();

            modelo.addElement("-- Seleccione Libro --");
            mapLibros.clear();

            while (rs.next()) {
                // El estado es una columna booleana en tu BD
                if (rs.getBoolean("estado")) {
                    Integer id = rs.getInt("idlibro");
                    String titulo = rs.getString("titulo");

                    modelo.addElement(titulo);
                    mapLibros.put(titulo, id);
                }
            }

            cboLibros.setModel(modelo); // Asigna el modelo al ComboBox
            rs.close();

        } catch (Exception e) {
            // 🚨 Muestra la excepción en caso de que falle la BD
            e.printStackTrace(); // Imprime el error completo en consola
            JOptionPane.showMessageDialog(this, "Error al cargar Libros: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarInformeAsignaciones() {
        ResultSet rsInforme = null;
        DefaultTableModel modelo = new DefaultTableModel();

        // 🚨 DEFINICIÓN DE COLUMNAS EXACTA SEGÚN TU REQUERIMIENTO 🚨
        modelo.addColumn("Título del Libro");
        modelo.addColumn("Nombre del Autor");

        try {
            // Llamar al nuevo método de la capa lógica
            rsInforme = objLibro.listarInformeAsignaciones();

            while (rsInforme.next()) {
                modelo.addRow(new Object[]{
                    // Los nombres de las columnas coinciden con el alias en la consulta SQL
                    rsInforme.getString("titulo_libro"),
                    rsInforme.getString("nombre_autor")
                });
            }

            // Asignar el modelo a la tabla
            tblAsignado.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el informe de asignaciones: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (rsInforme != null) {
                try {
                    rsInforme.close();
                } catch (SQLException ex) {
                    /* ignorar */ }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel11 = new javax.swing.JLabel();
        cmbAutores = new javax.swing.JComboBox<>();
        btnAutor = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        cboLibros = new javax.swing.JComboBox<>();
        btnBuscarLibro = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAsignado = new javax.swing.JTable();
        btnAsignar = new javax.swing.JButton();

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Autor:");

        cmbAutores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAutoresActionPerformed(evt);
            }
        });

        btnAutor.setText("...");
        btnAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutorActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Libro:");

        cboLibros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLibrosActionPerformed(evt);
            }
        });

        btnBuscarLibro.setText("...");
        btnBuscarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroActionPerformed(evt);
            }
        });

        tblAsignado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblAsignado);

        btnAsignar.setText("Asignar");
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel12)
                .addGap(27, 27, 27)
                .addComponent(cboLibros, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBuscarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(cmbAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cmbAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAutor)
                    .addComponent(jLabel12)
                    .addComponent(cboLibros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarLibro))
                .addGap(28, 28, 28)
                .addComponent(btnAsignar)
                .addGap(44, 44, 44)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutorActionPerformed
        java.awt.Window owner = javax.swing.SwingUtilities.getWindowAncestor(this);

        javax.swing.JDialog dialogoContenedor;

        if (owner instanceof java.awt.Frame) {
            dialogoContenedor = new javax.swing.JDialog((java.awt.Frame) owner, "Buscar Libro", true);
        } else if (owner instanceof java.awt.Dialog) {
            dialogoContenedor = new javax.swing.JDialog((java.awt.Dialog) owner, "Buscar Libro", true);
        } else {
            dialogoContenedor = new javax.swing.JDialog((java.awt.Frame) null, "Buscar Libro", true);
        }

        try {
            BuscarAutor panelLibro = new BuscarAutor(this, dialogoContenedor);

            dialogoContenedor.setContentPane(panelLibro);
            dialogoContenedor.revalidate();
            dialogoContenedor.pack();
            dialogoContenedor.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            dialogoContenedor.setLocationRelativeTo(owner);
            dialogoContenedor.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir la búsqueda de Libro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAutorActionPerformed

    private void cmbAutoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAutoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAutoresActionPerformed

    private void cboLibrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLibrosActionPerformed

    }//GEN-LAST:event_cboLibrosActionPerformed

    private void btnBuscarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroActionPerformed
        java.awt.Window owner = javax.swing.SwingUtilities.getWindowAncestor(this);

        javax.swing.JDialog dialogoContenedor;

        if (owner instanceof java.awt.Frame) {
            dialogoContenedor = new javax.swing.JDialog((java.awt.Frame) owner, "Buscar Libro", true);
        } else if (owner instanceof java.awt.Dialog) {
            dialogoContenedor = new javax.swing.JDialog((java.awt.Dialog) owner, "Buscar Libro", true);
        } else {
            dialogoContenedor = new javax.swing.JDialog((java.awt.Frame) null, "Buscar Libro", true);
        }

        try {
            BuscarLibro panelLibro = new BuscarLibro(this, dialogoContenedor);

            dialogoContenedor.setContentPane(panelLibro);
            dialogoContenedor.revalidate();
            dialogoContenedor.pack();
            dialogoContenedor.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            dialogoContenedor.setLocationRelativeTo(owner);
            dialogoContenedor.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir la búsqueda de Libro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarLibroActionPerformed

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        if (this.idLibroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Libro primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String autorNombre = (String) cmbAutores.getSelectedItem();
        if (autorNombre == null || autorNombre.startsWith("--")) { // "--" es el default item
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Autor válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Obtener IDs
        // El mapa mapAutoresSeleccionados contiene todos los autores disponibles, no solo el seleccionado.
        // Usaremos el ítem seleccionado en el ComboBox para obtener su ID.
        Integer idAutor = mapAutoresSeleccionados.get(autorNombre);

        if (idAutor == null) {
            JOptionPane.showMessageDialog(this, "Error: No se pudo obtener el ID del autor seleccionado.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 3. Llamar a la capa lógica para la inserción
            // El método en Libro.java espera (idLibro, idAutor, descripcion)
            objLibro.asignarAutor(this.idLibroSeleccionado, idAutor, ""); // Descripción vacía

            JOptionPane.showMessageDialog(this,
                    "Autor '" + autorNombre + "' asignado al libro con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // 4. Refrescar la tabla de Asignados
            listarAsignaciones(this.idLibroSeleccionado);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fallo al asignar: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void listarAsignaciones(Integer idLibro) {
        ResultSet rsAsignaciones = null;
        DefaultTableModel modelo = new DefaultTableModel();

        // 🚨 MODIFICACIÓN: La tabla debe mostrar el Nombre Completo
        modelo.addColumn("ID Autor");
        modelo.addColumn("Nombre Completo"); // ⬅️ Nombre completo del autor
        // Si quieres la descripción, añade: modelo.addColumn("Descripción"); 

        try {
            if (idLibro == null) {
                return;
            }

            rsAsignaciones = objLibro.listarAutoresPorLibro(idLibro);

            while (rsAsignaciones.next()) {
                // Construye el nombre completo del autor
                String nombreCompleto = rsAsignaciones.getString("nombres") + " " + rsAsignaciones.getString("apepaterno");

                modelo.addRow(new Object[]{
                    rsAsignaciones.getInt("idautor"),
                    nombreCompleto // ⬅️ Se inserta el nombre completo en la columna
                });
            }

            // 🚨 Asume que tu JTable se llama tblAsignado
            tblAsignado.setModel(modelo);

        } catch (Exception e) {
            // ...
        } finally {
            // ...
        }
    }

    public void setLibroSeleccionado(Integer idLibro, String titulo) {
        try {
            cargarLibrosActivos();

            cboLibros.setSelectedItem(titulo);

            JOptionPane.showMessageDialog(this,
                    "Libro seleccionado: " + titulo,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el libro seleccionado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setAutorSeleccionado(Integer idAutor, String nombreCompleto) {
        try {
            // Lógica para añadir/seleccionar el autor en este JDialog
            if (!mapAutoresSeleccionados.containsKey(nombreCompleto)) {
                cmbAutores.addItem(nombreCompleto);
                mapAutoresSeleccionados.put(nombreCompleto, idAutor);
            }

            // Seleccionar el ítem en el JComboBox
            cmbAutores.setSelectedItem(nombreCompleto);

            JOptionPane.showMessageDialog(this,
                    "Autor seleccionado: " + nombreCompleto,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el autor seleccionado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Módulo de asignación de autores a libros");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ManAsignarAutorLibro panelLibro = new ManAsignarAutorLibro();
        frame.getContentPane().add(panelLibro, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnAutor;
    private javax.swing.JButton btnBuscarLibro;
    private javax.swing.JComboBox<String> cboLibros;
    private javax.swing.JComboBox<String> cmbAutores;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAsignado;
    // End of variables declaration//GEN-END:variables
}
