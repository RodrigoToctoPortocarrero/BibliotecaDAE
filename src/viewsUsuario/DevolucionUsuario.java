/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewsUsuario;

import capaDatos.clsJDBC;
import java.sql.*;
import java.text.SimpleDateFormat; // Importante para formatear la fecha
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author VALENTINO
 */
public class DevolucionUsuario extends javax.swing.JPanel {
    clsJDBC jdbc = new clsJDBC();
    DefaultTableModel modelo;
    int idUsuarioLogueado;
    /**
     * Creates new form DevolucionUsuario
     */
    public DevolucionUsuario(int idUsuario) {
        
        this.idUsuarioLogueado = idUsuario;
        initComponents();
        configurarTabla();
        // Configurar formato del JDateChooser (Opcional pero recomendado)
        txtFecha.setDateFormatString("yyyy-MM-dd");
        // Llamamos al método pasando null para que traiga todo el historial
        cargarHistorial(null);
    }
    private void configurarTabla() {
        modelo = new DefaultTableModel();
        modelo.addColumn("F. Devolución");
        modelo.addColumn("Libro Entregado");
        modelo.addColumn("Estado Físico");
        modelo.addColumn("Multa Pagada");
        modelo.addColumn("Observaciones");
        tblResultados.setModel(modelo);
    }
    private void cargarHistorial(String fechaFiltro) {
        
        // Consulta SQL Base (Trae el historial del usuario)
        String sql = "SELECT d.fechadevolucionreal, l.titulo, dd.estado, dd.multa, dd.observaciones " +
                     "FROM DEVOLUCION d " +
                     "INNER JOIN PRESTAMO p ON d.idprestamo = p.idprestamo " +
                     "INNER JOIN DETALLE_DEVOLUCION dd ON d.iddevolucion = dd.iddevolucion " +
                     "INNER JOIN EJEMPLAR e ON dd.idejemplar = e.idejemplar " +
                     "INNER JOIN LIBROS l ON e.idlibro = l.idlibro " +
                     "WHERE p.idusuariolector = " + idUsuarioLogueado;

        // Si nos pasaron una fecha, agregamos el filtro
        if (fechaFiltro != null && !fechaFiltro.isEmpty()) {
            sql += " AND d.fechadevolucionreal = '" + fechaFiltro + "'";
        }
        
        // Ordenar por fecha más reciente
        sql += " ORDER BY d.fechadevolucionreal DESC";


        try {
            ResultSet rs = jdbc.consultarBD(sql);
            modelo.setRowCount(0); // Limpiar tabla antes de llenar
            boolean encontro = false;

            while (rs.next()) {
                encontro = true;
                
                boolean estadoBool = rs.getBoolean("estado");
                String estadoTexto = estadoBool ? "Buen Estado" : "DAÑADO";
                
                modelo.addRow(new Object[]{
                    rs.getDate("fechadevolucionreal"),
                    rs.getString("titulo"),
                    estadoTexto,
                    "S/. " + rs.getDouble("multa"),
                    rs.getString("observaciones")
                });
            }
            
            jdbc.desconectar();

            // Solo mostramos mensaje si se filtró por fecha y no hubo nada.
            // Si es la carga inicial (null) y no hay datos, simplemente sale vacía (es menos molesto).
            if (!encontro && fechaFiltro != null) {
                JOptionPane.showMessageDialog(this, "No tienes devoluciones registradas en la fecha: " + fechaFiltro);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void buscarDevolucion() {
        
        // 1. Obtener fecha del JDateChooser
        Date fechaSeleccionada = txtFecha.getDate();
        
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una fecha válida en el calendario.");
            return;
        }

        // 2. Convertir Date a String (Formato SQL: yyyy-MM-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaString = sdf.format(fechaSeleccionada);

        // 3. Consulta SQL
        String sql = "SELECT d.fechadevolucionreal, l.titulo, dd.estado, dd.multa, dd.observaciones " +
                     "FROM DEVOLUCION d " +
                     "INNER JOIN PRESTAMO p ON d.idprestamo = p.idprestamo " +
                     "INNER JOIN DETALLE_DEVOLUCION dd ON d.iddevolucion = dd.iddevolucion " +
                     "INNER JOIN EJEMPLAR e ON dd.idejemplar = e.idejemplar " +
                     "INNER JOIN LIBROS l ON e.idlibro = l.idlibro " +
                     "WHERE p.idusuariolector = " + idUsuarioLogueado + " " +
                     "AND d.fechadevolucionreal = '" + fechaString + "'";


        try {
            ResultSet rs = jdbc.consultarBD(sql);
            modelo.setRowCount(0); // Limpiar tabla
            boolean encontro = false;

            while (rs.next()) {
                encontro = true;
                
                boolean estadoBool = rs.getBoolean("estado");
                String estadoTexto = estadoBool ? "Buen Estado" : "DAÑADO";
                
                modelo.addRow(new Object[]{
                    rs.getDate("fechadevolucionreal"),
                    rs.getString("titulo"),
                    estadoTexto,
                    "S/. " + rs.getDouble("multa"),
                    rs.getString("observaciones")
                });
            }
            
            jdbc.desconectar();

            if (!encontro) {
                JOptionPane.showMessageDialog(this, "No tienes devoluciones registradas en la fecha: " + fechaString);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());
            e.printStackTrace();
        }
    }
   
    // --- LÓGICA COMÚN PARA LLENAR LA TABLA ---
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnDevolver = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblResultados = new javax.swing.JTable();
        txtFecha = new com.toedter.calendar.JDateChooser();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable2);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Devolución de Libro");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Fecha para buscar Devolucion:");

        btnDevolver.setBackground(new java.awt.Color(24, 118, 210));
        btnDevolver.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDevolver.setForeground(new java.awt.Color(255, 255, 255));
        btnDevolver.setText("Ver Libros Devueltos");
        btnDevolver.setBorder(null);
        btnDevolver.setBorderPainted(false);
        btnDevolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDevolverMouseClicked(evt);
            }
        });
        btnDevolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/descarga (1).jpg"))); // NOI18N

        tblResultados.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tblResultados);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDevolver, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(126, 126, 126))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDevolver, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDevolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDevolverMouseClicked

    }//GEN-LAST:event_btnDevolverMouseClicked

    private void btnDevolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverActionPerformed
     buscarDevolucion();
    }//GEN-LAST:event_btnDevolverActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDevolver;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable tblResultados;
    private com.toedter.calendar.JDateChooser txtFecha;
    // End of variables declaration//GEN-END:variables
}
