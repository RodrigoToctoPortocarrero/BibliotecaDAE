/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capaLogica;

import capaDatos.clsJDBC;

import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Percy Alexander
 */
public class Reportes {

    public static final String RUTA_REPORTES = "src\\Reportes\\";

    public JRViewer reporteInterno(String archivoReporte, Map<String, Object> parametros) throws Exception {
        try {
            clsJDBC objConexion = new clsJDBC();
            objConexion.conectar();
            JasperPrint reporte
                    = JasperFillManager.fillReport(this.RUTA_REPORTES + archivoReporte, parametros,
                            objConexion.getCon()
                    );
            JRViewer visor = new JRViewer(reporte);
            return visor;
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());

        }
        return null;
    }
}
