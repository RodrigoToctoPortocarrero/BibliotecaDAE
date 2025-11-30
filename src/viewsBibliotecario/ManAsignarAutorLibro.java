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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
    DefaultTableModel modeloTabla;

    /**
     * Creates new form asignarautor
     */
    public ManAsignarAutorLibro() {
        initComponents();
        cargarAutoresActivos();
        cargarLibrosActivos();

        configurarTabla();

    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel();

        // Definimos las columnas en este orden:
        // 0: ID Libro (OCULTO)
        // 1: Título Libro (VISIBLE)
        // 2: ID Autor (OCULTO)
        // 3: Nombre Autor (VISIBLE)
        // 4: Descripción (VISIBLE)
        modeloTabla.addColumn("idLibro");
        modeloTabla.addColumn("Libro");
        modeloTabla.addColumn("idAutor");
        modeloTabla.addColumn("Autor");
        modeloTabla.addColumn("Descripción");

        tblAsignado.setModel(modeloTabla);

        // --- TRUCO DE MAGIA: OCULTAR COLUMNAS DE IDs ---
        // Ocultar Columna 0 (ID Libro)
        tblAsignado.getColumnModel().getColumn(0).setMinWidth(0);
        tblAsignado.getColumnModel().getColumn(0).setMaxWidth(0);
        tblAsignado.getColumnModel().getColumn(0).setWidth(0);
        tblAsignado.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Ocultar Columna 2 (ID Autor)
        tblAsignado.getColumnModel().getColumn(2).setMinWidth(0);
        tblAsignado.getColumnModel().getColumn(2).setMaxWidth(0);
        tblAsignado.getColumnModel().getColumn(2).setWidth(0);
        tblAsignado.getColumnModel().getColumn(2).setPreferredWidth(0);
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
        try {
            ResultSet rs = objLibro.listarLibros();
            DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();

            modelo.addElement("-- Seleccione Libro --");
            mapLibros.clear();

            while (rs.next()) {
                if (rs.getBoolean("estado")) {
                    Integer id = rs.getInt("idlibro");
                    // 🚨 IMPORTANTE: Usamos .trim() para quitar espacios al inicio o final
                    String titulo = rs.getString("titulo");
                    if (titulo != null) {
                        titulo = titulo.trim();

                        modelo.addElement(titulo);
                        mapLibros.put(titulo, id);
                    }
                }
            }

            cboLibros.setModel(modelo);
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar Libros: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
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
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtdescripcion = new javax.swing.JTextArea();
        btnmas = new javax.swing.JButton();
        btnmenos = new javax.swing.JButton();

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

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Descripción:");

        txtdescripcion.setColumns(20);
        txtdescripcion.setRows(5);
        jScrollPane2.setViewportView(txtdescripcion);

        btnmas.setText("+");
        btnmas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmasActionPerformed(evt);
            }
        });

        btnmenos.setText("-");
        btnmenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmenosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(36, 36, 36)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnmas, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnmenos, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(27, 27, 27)
                        .addComponent(cboLibros, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(232, 232, 232)
                .addComponent(btnAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnmas, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnmenos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAsignar)
                .addGap(45, 45, 45))
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
        // 1. DETENER EDICIÓN (CRUCIAL): Si el usuario dejó el cursor en una celda, la tabla se bloquea.
        if (tblAsignado.isEditing()) {
            tblAsignado.getCellEditor().stopCellEditing();
        }

        // Usamos la variable global 'modeloTabla' que definiste en la clase
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "La tabla está vacía.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Procesar las " + modeloTabla.getRowCount() + " asignaciones?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int contExitos = 0;
        StringBuilder reporteErrores = new StringBuilder();

        System.out.println("--- INICIANDO PROCESO DE ASIGNACIÓN ---");

        // 2. BUCLE INVERSO (Cangrejo)
        for (int i = modeloTabla.getRowCount() - 1; i >= 0; i--) {

            String nombreLibro = modeloTabla.getValueAt(i, 1).toString();
            String nombreAutor = modeloTabla.getValueAt(i, 3).toString();

            try {
                // Captura de IDs
                Integer idLibroBD = Integer.parseInt(modeloTabla.getValueAt(i, 0).toString());
                Integer idAutorBD = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());
                String descBD = modeloTabla.getValueAt(i, 4).toString();

                // 3. INTENTO DE GUARDADO
                System.out.println("Intentando guardar: " + nombreLibro + " - " + nombreAutor);
                objLibro.asignarAutor(idLibroBD, idAutorBD, descBD);

                // 4. SI LLEGA AQUÍ, FUE ÉXITO -> BORRAR FILA
                System.out.println("   -> ÉXITO. Borrando fila " + i);
                modeloTabla.removeRow(i); // <--- ESTA LÍNEA BORRA LA FILA
                contExitos++;

            } catch (Exception e) {
                // 5. SI FALLA, ENTRA AQUÍ -> NO BORRA FILA
                System.out.println("   -> ERROR: " + e.getMessage());

                String errorMsg = e.getMessage().toLowerCase();

                // Construcción del reporte
                reporteErrores.append("--------------------------------------------------\n")
                        .append("LIBRO: ").append(nombreLibro).append("\n")
                        .append("AUTOR: ").append(nombreAutor).append("\n");

                if (errorMsg.contains("duplicate") || errorMsg.contains("llave duplicada") || errorMsg.contains("unique")) {
                    reporteErrores.append("ESTADO: Ya estaba registrado en la BD (No se duplicó).\n");
                } else {
                    reporteErrores.append("ERROR: ").append(e.getMessage()).append("\n");
                }
            }
        }

        System.out.println("--- FIN DEL PROCESO ---");

        // 6. FORZAR REFRESCO VISUAL (Por si acaso la tabla se quedó "pegada")
        tblAsignado.revalidate();
        tblAsignado.repaint();

        // 7. RESULTADOS
        if (reporteErrores.length() == 0) {
            JOptionPane.showMessageDialog(this, "¡Éxito Total! Se guardaron " + contExitos + " registros.", "Finalizado", JOptionPane.INFORMATION_MESSAGE);
            txtdescripcion.setText("");
        } else {
            javax.swing.JTextArea areaTexto = new javax.swing.JTextArea(12, 50);
            areaTexto.setText("Se guardaron correctamente: " + contExitos + "\n"
                    + "Quedan en la tabla (no se pudieron guardar):\n"
                    + reporteErrores.toString());
            areaTexto.setEditable(false);
            JOptionPane.showMessageDialog(this, new javax.swing.JScrollPane(areaTexto), "Reporte de Conflictos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void btnmasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmasActionPerformed
        // --- 1. OBTENER LIBRO ---
        String nombreLibro = (String) cboLibros.getSelectedItem();

        if (nombreLibro == null || nombreLibro.trim().isEmpty() || nombreLibro.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un Libro del desplegable.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        nombreLibro = nombreLibro.trim(); // Limpiamos espacios

        // --- LÓGICA BLINDADA PARA OBTENER ID ---
        // Intento 1: Búsqueda directa en el mapa
        if (mapLibros.containsKey(nombreLibro)) {
            this.idLibroSeleccionado = mapLibros.get(nombreLibro);
        } else {
            // Intento 2 (Respaldo): Buscar ignorando mayúsculas o espacios extra
            // Esto se activa si el mapa falló, para forzar el encuentro del ID
            boolean encontrado = false;
            for (String key : mapLibros.keySet()) {
                if (key.equalsIgnoreCase(nombreLibro)) {
                    this.idLibroSeleccionado = mapLibros.get(key);
                    encontrado = true;
                    break;
                }
            }

            // Si después de buscar no lo encontramos y tampoco vino del botón buscar:
            if (!encontrado && this.idLibroSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Error: No se encuentra el ID para el libro: '" + nombreLibro + "'\nIntente recargar la ventana.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // --- 2. OBTENER AUTOR ---
        String nombreAutor = (String) cmbAutores.getSelectedItem();
        if (nombreAutor == null || nombreAutor.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Autor.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Aseguramos que el nombre del autor también esté limpio
        nombreAutor = nombreAutor.trim();
        Integer idAutor = mapAutoresSeleccionados.get(nombreAutor);

        if (idAutor == null) {
            // Búsqueda de respaldo para Autor también
            for (String key : mapAutoresSeleccionados.keySet()) {
                if (key.equalsIgnoreCase(nombreAutor)) {
                    idAutor = mapAutoresSeleccionados.get(key);
                    break;
                }
            }

            if (idAutor == null) {
                JOptionPane.showMessageDialog(this, "Error al obtener ID del autor.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // --- 3. DESCRIPCIÓN ---
        String descripcion = txtdescripcion.getText().trim();
        if (descripcion.isEmpty()) {
            descripcion = "Sin descripción";
        }

        // --- 4. VALIDAR DUPLICADOS EN TABLA ---
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            try {
                int idLibroTabla = Integer.parseInt(modeloTabla.getValueAt(i, 0).toString());
                int idAutorTabla = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());

                if (idLibroTabla == this.idLibroSeleccionado && idAutorTabla == idAutor) {
                    JOptionPane.showMessageDialog(this, "El autor '" + nombreAutor + "' ya está en la lista para este libro.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                // Ignorar error de parseo si ocurre
            }
        }

        // --- 5. AGREGAR A LA TABLA ---
        modeloTabla.addRow(new Object[]{
            this.idLibroSeleccionado, // Col 0: ID Libro (OCULTO)
            nombreLibro, // Col 1: Título (VISIBLE)
            idAutor, // Col 2: ID Autor (OCULTO)
            nombreAutor, // Col 3: Nombre Autor (VISIBLE)
            descripcion // Col 4: Descripción (VISIBLE)
        });

        // Limpiar solo descripción para permitir agregar otro autor al mismo libro rápido
        txtdescripcion.setText("");
    }//GEN-LAST:event_btnmasActionPerformed

    private void btnmenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmenosActionPerformed
        // 1. Verificar si hay una fila seleccionada
        int filaSeleccionada = tblAsignado.getSelectedRow();

        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Preguntar confirmación (Opcional, pero recomendado)
        // int confirm = JOptionPane.showConfirmDialog(this, "¿Quitar este autor de la lista?", "Confirmar", JOptionPane.YES_NO_OPTION);
        // if (confirm != JOptionPane.YES_OPTION) return;
        // 3. Eliminar la fila del modelo
        modeloTabla.removeRow(filaSeleccionada);
    }//GEN-LAST:event_btnmenosActionPerformed

    public void setLibroSeleccionado(Integer idLibro, String titulo) {
        try {
            this.idLibroSeleccionado = idLibro; // GUARDAR EL ID

            // Sincronizar el combo box si existe el item
            cboLibros.setSelectedItem(titulo);

            // Limpiar la tabla de autores pendientes porque cambiamos de libro
            if (modeloTabla != null) {
                modeloTabla.setRowCount(0);
            }

            JOptionPane.showMessageDialog(this, "Libro seleccionado: " + titulo + " (ID: " + idLibro + ")");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar libro: " + e.getMessage());
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnAutor;
    private javax.swing.JButton btnBuscarLibro;
    private javax.swing.JButton btnmas;
    private javax.swing.JButton btnmenos;
    private javax.swing.JComboBox<String> cboLibros;
    private javax.swing.JComboBox<String> cmbAutores;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblAsignado;
    private javax.swing.JTextArea txtdescripcion;
    // End of variables declaration//GEN-END:variables
}
