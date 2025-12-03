package viewsBibliotecario;

import capaLogica.Prestamo;
import capaLogica.Sesion;
import capaLogica.Usuarios;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fabian Antonio Carrasco Vera
 */
public class Prestamos extends javax.swing.JPanel {

    /**
     * Creates new form Prestamos
     */
    public Prestamos() {
        initComponents();

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID EJEMPLAR");
        modelo.addColumn("NRO EJEMPLAR");
        modelo.addColumn("TÍTULO");
        modelo.addColumn("OBSERVACIÓN");

        tblDetallePrestamo.setModel(modelo);

        tblDetallePrestamo.getTableHeader().setReorderingAllowed(false); // No mover columnas
        tblDetallePrestamo.getTableHeader().setResizingAllowed(false);   // No cambiar tamaño
    }

    public void agregarEjemplar(int idEjemplar, String nroEjemplar, String titulo, String obs) {

        DefaultTableModel modelo = (DefaultTableModel) tblDetallePrestamo.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (Integer.parseInt(modelo.getValueAt(i, 0).toString()) == idEjemplar) {
                JOptionPane.showMessageDialog(this, "Este ejemplar ya fue agregado.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (modelo.getRowCount() >= 3) {
            JOptionPane.showMessageDialog(this,
                    "Solo puede agregar un máximo de 3 ejemplares al préstamo.",
                    "Límite alcanzado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (obs == null) {
            obs = "";
        }
        obs = obs.trim();

        modelo.addRow(new Object[]{
            idEjemplar,
            nroEjemplar,
            titulo,
            obs
        });
    }

    private void limpiarFormulario() {
        txtLector.setText("");
        jDateChooser1.setDate(null);

        DefaultTableModel modelo = (DefaultTableModel) tblDetallePrestamo.getModel();
        modelo.setRowCount(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnBuscarEjemplar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetallePrestamo = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnRegistrarPrestamo = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        btnEliminarEjemplar = new javax.swing.JButton();
        txtLector = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Número de ejemplar:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("PRÉSTAMOS");

        btnBuscarEjemplar.setBackground(new java.awt.Color(24, 118, 210));
        btnBuscarEjemplar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscarEjemplar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarEjemplar.setText("...");
        btnBuscarEjemplar.setBorder(null);
        btnBuscarEjemplar.setBorderPainted(false);
        btnBuscarEjemplar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarEjemplarMouseClicked(evt);
            }
        });
        btnBuscarEjemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEjemplarActionPerformed(evt);
            }
        });

        tblDetallePrestamo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDetallePrestamo);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Lector:");

        btnRegistrarPrestamo.setBackground(new java.awt.Color(24, 118, 210));
        btnRegistrarPrestamo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegistrarPrestamo.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarPrestamo.setText("Registrar Préstamo");
        btnRegistrarPrestamo.setBorder(null);
        btnRegistrarPrestamo.setBorderPainted(false);
        btnRegistrarPrestamo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarPrestamoMouseClicked(evt);
            }
        });
        btnRegistrarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarPrestamoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Fecha devolución:");

        btnEliminarEjemplar.setBackground(new java.awt.Color(24, 118, 210));
        btnEliminarEjemplar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminarEjemplar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarEjemplar.setText("Eliminar Ejemplar");
        btnEliminarEjemplar.setBorder(null);
        btnEliminarEjemplar.setBorderPainted(false);
        btnEliminarEjemplar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarEjemplarMouseClicked(evt);
            }
        });
        btnEliminarEjemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEjemplarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRegistrarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminarEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLector, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(56, 56, 56)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(247, 247, 247))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel2)
                        .addComponent(txtLector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btnBuscarEjemplar))
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarEjemplarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarEjemplarMouseClicked
        BuscarEjemplar buscador = new BuscarEjemplar(this);
        buscador.setModal(true);
        buscador.setLocationRelativeTo(this);
        buscador.setVisible(true);
    }//GEN-LAST:event_btnBuscarEjemplarMouseClicked

    private void btnBuscarEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEjemplarActionPerformed

    }//GEN-LAST:event_btnBuscarEjemplarActionPerformed

    private void btnRegistrarPrestamoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarPrestamoMouseClicked

    }//GEN-LAST:event_btnRegistrarPrestamoMouseClicked

    private void btnRegistrarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarPrestamoActionPerformed
        try {

            if (txtLector.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un lector.",
                        "Falta Lector",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (jDateChooser1.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una fecha estimada de devolución.",
                        "Fecha Faltante",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date fechaSeleccionada = jDateChooser1.getDate();
            Date hoy = new Date();

            SimpleDateFormat formatoCorto = new SimpleDateFormat("yyyy-MM-dd");
            fechaSeleccionada = formatoCorto.parse(formatoCorto.format(fechaSeleccionada));
            hoy = formatoCorto.parse(formatoCorto.format(hoy));

            if (fechaSeleccionada.before(hoy)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de devolución no puede ser menor a la fecha actual.",
                        "Fecha Inválida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tblDetallePrestamo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Debe agregar al menos un ejemplar.",
                        "Sin Ejemplares",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tblDetallePrestamo.getRowCount() > 3) {
                JOptionPane.showMessageDialog(this,
                        "Solo puede prestar un máximo de 3 ejemplares por préstamo.",
                        "Límite Excedido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Prestamo objPrestamo = new Prestamo();
            Usuarios objUsuarios = new Usuarios();

            int idPrestamo = objPrestamo.generarCodigoPrestamo();
            int idLector = objUsuarios.obtenerIdLectorPorNombre(txtLector.getText());

            if (!objPrestamo.usuarioEstaVigente(idLector)) {
                JOptionPane.showMessageDialog(this,
                        "El usuario lector no está VIGENTE.\n"
                        + "No puede realizar préstamos hasta que sea reactivado.",
                        "Usuario No Vigente",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (objPrestamo.tienePrestamosActivos(idLector)) {
                JOptionPane.showMessageDialog(this,
                        "El lector tiene préstamos activos.\n"
                        + "Debe devolver los libros antes de solicitar otro préstamo.",
                        "Préstamo Activo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (objPrestamo.tieneMultasPendientes(idLector)) {
                JOptionPane.showMessageDialog(this,
                        "El lector tiene multas pendientes.\n"
                        + "Debe cancelar las multas antes de solicitar préstamos.",
                        "Multas Pendientes",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idBibliotecario = Sesion.getUsuario().getIdusuario();

            java.util.Date fechaUtil = jDateChooser1.getDate();
            java.sql.Date fechaSQL = new java.sql.Date(fechaUtil.getTime());
            String fechaDevString = formatoCorto.format(fechaUtil);

            int op = JOptionPane.showOptionDialog(
                    null,
                    "¿Está seguro de registrar este préstamo?\n\n"
                    + "Lector: " + txtLector.getText() + "\n"
                    + "Ejemplares: " + tblDetallePrestamo.getRowCount() + "\n"
                    + "Fecha Devolución: " + fechaDevString,
                    "Confirmar Préstamo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Sí", "No"},
                    "Sí"
            );

            if (op != JOptionPane.YES_OPTION) {
                return;
            }

            objPrestamo.registrarPrestamo(
                    idPrestamo,
                    idLector,
                    idBibliotecario,
                    fechaSQL,
                    tblDetallePrestamo
            );

            JOptionPane.showMessageDialog(this,
                    "✓ Préstamo registrado correctamente.\n\n"
                    + "ID Préstamo: " + idPrestamo + "\n"
                    + "Fecha Préstamo: HOY\n"
                    + "Fecha Devolución Estimada: " + fechaDevString + "\n"
                    + "Ejemplares prestados: " + tblDetallePrestamo.getRowCount(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar préstamo:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnRegistrarPrestamoActionPerformed

    private void btnEliminarEjemplarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarEjemplarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarEjemplarMouseClicked

    private void btnEliminarEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEjemplarActionPerformed
        int fila = tblDetallePrestamo.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un ejemplar para eliminar.",
                    "Ninguna fila seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showOptionDialog(
                null,
                "¿Desea eliminar el ejemplar seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Sí", "No"},
                "Sí"
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tblDetallePrestamo.getModel();
        modelo.removeRow(fila);

        JOptionPane.showMessageDialog(this,
                "Ejemplar eliminado.",
                "Eliminado",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnEliminarEjemplarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarEjemplar;
    private javax.swing.JButton btnEliminarEjemplar;
    private javax.swing.JButton btnRegistrarPrestamo;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDetallePrestamo;
    private javax.swing.JTextField txtLector;
    // End of variables declaration//GEN-END:variables
}
