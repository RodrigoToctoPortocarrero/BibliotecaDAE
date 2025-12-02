/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewsBibliotecario;

import javax.swing.JFrame;
import capaLogica.Libro;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import javax.swing.JFrame;
import java.sql.SQLException;
import java.awt.event.KeyAdapter; // Necesario para los eventos KeyReleased
import java.awt.event.KeyEvent;

/**
 *
 * @author Percy Alexander
 */
public class BuscarLibro extends javax.swing.JPanel {

    Libro objLibro = new Libro(); // Instancia de la capa lógica
    private ManAsignarAutorLibro panelAsignarLibro; // Referencia al delegado
    private javax.swing.JDialog dialogoContenedor;

    /**
     * Creates new form BuscarLibro
     */
    public BuscarLibro(ManAsignarAutorLibro principal, javax.swing.JDialog contenedor) { // 👈 ESTE CONSTRUCTOR
        initComponents();
        this.panelAsignarLibro = principal; // Asigna la referencia del delegado
        this.dialogoContenedor = contenedor;
        listarLibros(); // Opcional: listar al inicio
    }

    private void listarLibros() {
        ResultSet rsLibros = null;
        DefaultTableModel modelo = new DefaultTableModel();

        // Definir las 6 columnas que devuelve el SQL de Libro.filtrarLibros()
        modelo.addColumn("ID"); // 0
        modelo.addColumn("TÍTULO"); // 1
        modelo.addColumn("AÑO"); // 2
        modelo.addColumn("EDITORIAL"); // 3
        modelo.addColumn("CATEGORÍA"); // 4
        modelo.addColumn("ESTADO"); // 5

        try {
            // 1. Recolección de parámetros de búsqueda
            String titulo = txtNombre.getText(); // Nombre para el Título
            String anio = txtAño.getText();
            // String estado = cbxEstado.getSelectedItem().toString(); // ❌ ELIMINADO: ahora usaremos "Todos" por defecto, o el valor que necesites
            String estado = "Todos"; // Valor fijo después de eliminar el JComboBox
            String nomEditorial = txtEditorial.getText();
            String nomCategoria = txtCategoria.getText();

            // 2. Llamar al método de filtrado en la capa lógica
            // NOTA: Si tu capa lógica (Libro.filtrarLibros) maneja el parámetro de estado
            // como opcional o acepta "Todos", esto funcionará.
            rsLibros = objLibro.filtrarLibros(titulo, anio, estado, nomEditorial, nomCategoria);

            // 3. Llenar la tabla con los resultados
            while (rsLibros.next()) {
                String estadoStr = rsLibros.getBoolean("estado") ? "Activo" : "Inactivo";

                modelo.addRow(new Object[]{
                    rsLibros.getInt("idlibro"),
                    rsLibros.getString("titulo"),
                    rsLibros.getString("aniopublicacion"),
                    rsLibros.getString("nombre_editorial"),
                    rsLibros.getString("nombre_categoria"),
                    estadoStr // Columna 5
                });
            }

            tblListadoLibros.setModel(modelo);
            txtTotal.setText(String.valueOf(tblListadoLibros.getRowCount())); // Actualizar contador

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar libros: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (rsLibros != null) {
                try {
                    rsLibros.close();
                } catch (SQLException ex) {
                    /* ignorar */ }
            }
        }
    }

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblListadoLibros.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Libro de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Validar el estado (Columna 5)
            String estadoLibro = tblListadoLibros.getValueAt(fila, 5).toString();
            if (!estadoLibro.equalsIgnoreCase("Activo")) {
                JOptionPane.showMessageDialog(this, "Solo se pueden seleccionar Libros Activos.", "Libro Inactivo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Obtener ID (Columna 0) y Título (Columna 1)
            String idString = tblListadoLibros.getValueAt(fila, 0).toString();
            Integer idLibro = Integer.parseInt(idString);
            String titulo = tblListadoLibros.getValueAt(fila, 1).toString();

            // 3. Llamada al delegado y cierre de ventana
            if (panelAsignarLibro != null) {
                panelAsignarLibro.setLibroSeleccionado(idLibro, titulo);
                dialogoContenedor.dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar el libro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        txtNombre = new javax.swing.JTextField();
        txtAño = new javax.swing.JTextField();
        txtEditorial = new javax.swing.JTextField();
        txtCategoria = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListadoLibros = new javax.swing.JTable();
        btnSeleccionar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
        });

        txtAño.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAñoKeyReleased(evt);
            }
        });

        txtEditorial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEditorialKeyReleased(evt);
            }
        });

        txtCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCategoriaKeyReleased(evt);
            }
        });

        jLabel1.setText("Total");

        txtTotal.setText("Total");

        tblListadoLibros.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblListadoLibros);

        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre:");

        jLabel3.setText("Año:");

        jLabel4.setText("Editorial:");

        jLabel5.setText("Categoría:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addComponent(txtTotal)
                .addContainerGap(332, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(txtAño, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 63, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(btnSeleccionar)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTotal))
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        listarLibros();
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtAñoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAñoKeyReleased
        listarLibros();
    }//GEN-LAST:event_txtAñoKeyReleased

    private void txtEditorialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEditorialKeyReleased
        listarLibros();
    }//GEN-LAST:event_txtEditorialKeyReleased

    private void txtCategoriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCategoriaKeyReleased
        listarLibros();
    }//GEN-LAST:event_txtCategoriaKeyReleased
    /*
    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed

    }//GEN-LAST:event_btnSeleccionarActionPerformed
*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListadoLibros;
    private javax.swing.JTextField txtAño;
    private javax.swing.JTextField txtCategoria;
    private javax.swing.JTextField txtEditorial;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
