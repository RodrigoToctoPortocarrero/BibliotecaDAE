/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewsUsuario;

import capaLogica.Reportes;
import capaLogica.Sesion;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author FABIAN VERA
 */
public class Reporte11 extends javax.swing.JPanel {

    /**
     * Creates new form Reporte11
     */
    public Reporte11() {
        initComponents();
        try {
            int idUsuarioLector = Sesion.getUsuario().getIdusuario();
            this.vistaReporte.setLayout(new BorderLayout());
            this.vistaReporte.removeAll();

            Map<String, Object> parametros = new HashMap<>();

            parametros.put("idUsuarioLector", idUsuarioLector);

            JRViewer objReporte = new Reportes().reporteInterno("rp11_CarrascoVeraFabianAntonio.jasper", parametros);

            this.vistaReporte.add(objReporte);
            this.vistaReporte.revalidate();
            this.vistaReporte.repaint();
            objReporte.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "ERROR en Reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        vistaReporte = new javax.swing.JDesktopPane();

        javax.swing.GroupLayout vistaReporteLayout = new javax.swing.GroupLayout(vistaReporte);
        vistaReporte.setLayout(vistaReporteLayout);
        vistaReporteLayout.setHorizontalGroup(
            vistaReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 625, Short.MAX_VALUE)
        );
        vistaReporteLayout.setVerticalGroup(
            vistaReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vistaReporte)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vistaReporte)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane vistaReporte;
    // End of variables declaration//GEN-END:variables
}
