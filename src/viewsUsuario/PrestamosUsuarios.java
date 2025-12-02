/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewsUsuario;

import capaLogica.Prestamo;
import capaLogica.Sesion;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fabian Antonio Carrasco Vera
 */
public class PrestamosUsuarios extends javax.swing.JPanel {

    ResultSet rs = null;
    Prestamo objP = new Prestamo();
    int idUsuarioActual = Sesion.getUsuario().getIdusuario();

    public PrestamosUsuarios() {
        initComponents();
        cargarPrestamosIniciales();
    }

    private void cargarPrestamosIniciales() {
        try {
            rs = objP.listarPrestamosDeUsuario(idUsuarioActual);
            mostrarPrestamosEnTabla(rs);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void mostrarPrestamosEnTabla(ResultSet rs) throws Exception {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Préstamo");
        modelo.addColumn("Fecha Préstamo");
        modelo.addColumn("Fecha Devolución Estimada");
        modelo.addColumn("Estado");
        modelo.addColumn("Libros");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        while (rs.next()) {

            String fechaPrestamo = sdf.format(rs.getDate("fechaprestamo"));
            String fechaDevEst = sdf.format(rs.getDate("fechadevolucionestimada"));

            modelo.addRow(new Object[]{
                rs.getInt("idprestamo"),
                fechaPrestamo,
                fechaDevEst,
                rs.getString("estado"),
                rs.getString("libros") // viene desde la consulta SQL
            });
        }

        tblPrestamos.setModel(modelo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnLibrosPrestados = new javax.swing.JButton();
        jdcFecha = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(730, 360));
        setMinimumSize(new java.awt.Dimension(730, 360));
        setPreferredSize(new java.awt.Dimension(730, 360));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("PRÉSTAMOS");

        tblPrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblPrestamos);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Fecha para buscar Préstamos:");

        btnLibrosPrestados.setBackground(new java.awt.Color(24, 118, 210));
        btnLibrosPrestados.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLibrosPrestados.setForeground(new java.awt.Color(255, 255, 255));
        btnLibrosPrestados.setText("Ver Libros Prestados");
        btnLibrosPrestados.setBorder(null);
        btnLibrosPrestados.setBorderPainted(false);
        btnLibrosPrestados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLibrosPrestadosMouseClicked(evt);
            }
        });
        btnLibrosPrestados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLibrosPrestadosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLibrosPrestados, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(143, 143, 143))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLibrosPrestados, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/prueba.jpeg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");
        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnLibrosPrestadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLibrosPrestadosMouseClicked

    }//GEN-LAST:event_btnLibrosPrestadosMouseClicked

    private void btnLibrosPrestadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLibrosPrestadosActionPerformed
        try {
            String fechaString = null;

            // VALIDACIÓN DE FECHA (solo si seleccionó una)
            if (jdcFecha.getDate() != null) {

                Date fechaElegida = jdcFecha.getDate();
                Date hoy = new Date();

                // Validar fecha futura
                if (fechaElegida.after(hoy)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "La fecha no puede ser futura.",
                            "Fecha inválida",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Convertir fecha a String con formato YYYY-MM-DD (para SQL)
                SimpleDateFormat sdfSQL = new SimpleDateFormat("yyyy-MM-dd");
                fechaString = sdfSQL.format(fechaElegida);
            }

            // CONSULTA A BD
            rs = objP.buscarPrestamos(idUsuarioActual, fechaString);

            // Mostrar datos en tabla
            mostrarPrestamosEnTabla(rs);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }//GEN-LAST:event_btnLibrosPrestadosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLibrosPrestados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFecha;
    private javax.swing.JTable tblPrestamos;
    // End of variables declaration//GEN-END:variables
}
