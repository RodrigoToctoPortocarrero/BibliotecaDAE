package viewsBibliotecario;

import capaLogica.Ejemplar;
import java.awt.Frame;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fabian Antonio Carrasco Vera
 */
public class BuscarEjemplar extends javax.swing.JDialog {

    Ejemplar objEjemplar = new Ejemplar();

    private Prestamos panelPrestamo;

    public BuscarEjemplar(Prestamos panelPrestamo) {
        super((Frame) null, true); 
        initComponents();
        this.panelPrestamo = panelPrestamo;
        listarEjemplares();
    }

    private void limpiar() {
        txtObser.setText("");
        txtLibro.setText("");
        listarEjemplares();
        txtLibro.requestFocus();
    }

    private void listarEjemplares() {
        ResultSet rs = null;

        //PARA NO EDITAR TABLA
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID EJEMPLAR", "NRO EJEMPLAR", "TÍTULO", "ESTADO"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            String filtro = txtLibro.getText();
            rs = objEjemplar.filtrarNombre(filtro);

            while (rs.next()) {

                String estadoStr = rs.getBoolean("estado_devolucion") ? "Disponible" : "No Disponible";

                modelo.addRow(new Object[]{
                    rs.getInt("idejemplar"),
                    rs.getString("nroejemplar"),
                    rs.getString("titulo"),
                    estadoStr
                });
            }

            tblEjemplares.setModel(modelo);
            // BLOQUEAR EDITORES
            tblEjemplares.setDefaultEditor(Object.class, null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al listar ejemplares: " + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtLibro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEjemplares = new javax.swing.JTable();
        btnAgregarPrestamo = new javax.swing.JButton();
        txtObser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("BUSCAR EJEMPLAR");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Libro:");

        txtLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLibroActionPerformed(evt);
            }
        });
        txtLibro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLibroKeyReleased(evt);
            }
        });

        tblEjemplares.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblEjemplares);

        btnAgregarPrestamo.setBackground(new java.awt.Color(24, 118, 210));
        btnAgregarPrestamo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAgregarPrestamo.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarPrestamo.setText("Agregar");
        btnAgregarPrestamo.setBorder(null);
        btnAgregarPrestamo.setBorderPainted(false);
        btnAgregarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPrestamoActionPerformed(evt);
            }
        });

        txtObser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtObserActionPerformed(evt);
            }
        });
        txtObser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtObserKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Observación:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 157, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnAgregarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(139, 139, 139))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtObser, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtObser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPrestamoActionPerformed

        int fila = tblEjemplares.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Ejemplar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String obser = txtObser.getText().trim();

        if (obser.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar una observación antes de agregar el ejemplar.",
                    "Falta observación",
                    JOptionPane.WARNING_MESSAGE);
            txtObser.requestFocus();
            return;
        }

        // SOLO LETRAS Y ESPACIOS
        if (!obser.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            JOptionPane.showMessageDialog(this,
                    "La observación solo debe contener texto (letras y espacios).",
                    "Texto inválido",
                    JOptionPane.WARNING_MESSAGE);
            txtObser.requestFocus();
            return;
        }

        try {
            int idEjemplar = Integer.parseInt(tblEjemplares.getValueAt(fila, 0).toString());
            String nroEjemplar = tblEjemplares.getValueAt(fila, 1).toString();
            String titulo = tblEjemplares.getValueAt(fila, 2).toString();
            String obs = txtObser.getText().trim();

            if (panelPrestamo != null) {
                panelPrestamo.agregarEjemplar(idEjemplar, nroEjemplar, titulo, obs);
                limpiar();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar ejemplar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAgregarPrestamoActionPerformed

    private void txtLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLibroActionPerformed
    }//GEN-LAST:event_txtLibroActionPerformed

    private void txtLibroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLibroKeyReleased
        listarEjemplares();
    }//GEN-LAST:event_txtLibroKeyReleased

    private void txtObserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObserActionPerformed

    private void txtObserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObserKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObserKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarPrestamo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEjemplares;
    private javax.swing.JTextField txtLibro;
    private javax.swing.JTextField txtObser;
    // End of variables declaration//GEN-END:variables
}
