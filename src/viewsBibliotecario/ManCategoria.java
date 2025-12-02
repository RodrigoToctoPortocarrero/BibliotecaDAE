/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewsBibliotecario;

import capaDatos.clsJDBC;
import capaLogica.Categoria;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Percy Castañeda
 */
public class ManCategoria extends javax.swing.JPanel {

    Categoria cat = new Categoria();

    public ManCategoria() {
        initComponents();
        ListarCategoria();
        habilitarBotones("INICIAL");
    }

    private void habilitarBotones(String estado) {
        switch (estado) {
            case "INICIAL":
                // Estado inicial: solo NUEVO está habilitado
                btnNuevo.setEnabled(true);
                btnBuscar.setEnabled(true);
                btnEliminar.setEnabled(false);
                btnActualizar.setEnabled(false);
                btnDarBaja.setEnabled(false);
                btnLimpiar.setEnabled(true);

                // Campos deshabilitados excepto código para buscar
                txtCodigoCategoria.setEditable(true);
                txtNombreCategoria.setEditable(false);
                txtDescripcion.setEditable(false);
                checkVigencia.setEnabled(false);
                break;

            case "CREANDO":
                // Modo creación: solo GUARDAR y LIMPIAR habilitados
                btnNuevo.setEnabled(true); // Cambiará a "GUARDAR"
                btnBuscar.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnActualizar.setEnabled(false);
                btnDarBaja.setEnabled(false);
                btnLimpiar.setEnabled(true);

                // Campos habilitados para edición
                txtCodigoCategoria.setEditable(false); // Código autogenerado
                txtNombreCategoria.setEditable(true);
                txtDescripcion.setEditable(true);
                checkVigencia.setEnabled(true);
                break;

            case "CONSULTADO":
                // Después de buscar: habilitar acciones sobre el registro
                btnNuevo.setEnabled(true);
                btnBuscar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnActualizar.setEnabled(true);
                btnDarBaja.setEnabled(true);
                btnLimpiar.setEnabled(true);

                // Campos habilitados para edición (excepto código)
                txtCodigoCategoria.setEditable(false);
                txtNombreCategoria.setEditable(true);
                txtDescripcion.setEditable(true);
                checkVigencia.setEnabled(true);
                break;

            case "PROCESANDO":
                // Durante operaciones de BD: deshabilitar todo
                btnNuevo.setEnabled(false);
                btnBuscar.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnActualizar.setEnabled(false);
                btnDarBaja.setEnabled(false);
                btnLimpiar.setEnabled(false);

                txtCodigoCategoria.setEditable(false);
                txtNombreCategoria.setEditable(false);
                txtDescripcion.setEditable(false);
                checkVigencia.setEnabled(false);
                break;
        }
    }

// ============================================
// MÉTODOS ACTUALIZADOS CON VALIDACIONES
// ============================================
    private void LimpiarCampos() {
        txtCodigoCategoria.setText("");
        txtNombreCategoria.setText("");
        txtDescripcion.setText("");
        checkVigencia.setSelected(false);

        // Restaurar estado del botón NUEVO si estaba en modo GUARDAR
        if (btnNuevo.getText().equals("GUARDAR")) {
            btnNuevo.setText("NUEVO");
        }

        // Volver al estado inicial
        habilitarBotones("INICIAL");
    }

    private void ListarCategoria() {
        try {

            ResultSet categorias = cat.listarCategoria();
            DefaultTableModel tabla = new DefaultTableModel();
            Vector fila = null;
            String vigente = "";
            tabla.addColumn("codigo categoria");
            tabla.addColumn("nombre categoria");
            tabla.addColumn("descripcion");
            tabla.addColumn("Vigencia");
            while (categorias.next()) {
                int codigo = categorias.getInt("idcategoria");
                String nombre = categorias.getString("nombrecategoria");
                String descripcion = categorias.getString("descripcion");
                Boolean vig = categorias.getBoolean("estado");
                if (vig) {
                    vigente = "Vigente";
                } else {
                    vigente = "No Vigente";
                }

                fila = new Vector();
                fila.add(0, codigo);
                fila.add(1, nombre);
                fila.add(2, descripcion);
                fila.add(3, vigente);

                tabla.addRow(fila);
            }

            tblCategoria.setModel(tabla);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar categoria: " + e.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategoria = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtCodigoCategoria = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombreCategoria = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        btnEliminar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnDarBaja = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        checkVigencia = new javax.swing.JCheckBox();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnNuevo.setBackground(new java.awt.Color(24, 118, 210));
        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/registrarMarca.png"))); // NOI18N
        btnNuevo.setText("NUEVO");
        btnNuevo.setBorder(null);
        btnNuevo.setBorderPainted(false);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        tblCategoria.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tblCategoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCategoriaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCategoria);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Codigo Categoria");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Nombre categoria");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Descripcion");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        btnEliminar.setBackground(new java.awt.Color(24, 118, 210));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminarMarca.png"))); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.setBorder(null);
        btnEliminar.setBorderPainted(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(24, 118, 210));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscarMarca.png"))); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.setBorder(null);
        btnBuscar.setBorderPainted(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnActualizar.setBackground(new java.awt.Color(24, 118, 210));
        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/modificarMarca.png"))); // NOI18N
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.setBorder(null);
        btnActualizar.setBorderPainted(false);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(24, 118, 210));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/limpiarMarca.png"))); // NOI18N
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.setBorder(null);
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnDarBaja.setBackground(new java.awt.Color(24, 118, 210));
        btnDarBaja.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDarBaja.setForeground(new java.awt.Color(255, 255, 255));
        btnDarBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/darBajaMarca.png"))); // NOI18N
        btnDarBaja.setText("DAR BAJA");
        btnDarBaja.setBorder(null);
        btnDarBaja.setBorderPainted(false);
        btnDarBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDarBajaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Vigencia");

        checkVigencia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        checkVigencia.setText("Vigente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(52, 52, 52)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(checkVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 97, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(55, 55, 55)
                                .addComponent(txtCodigoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(btnDarBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jLabel2)
                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDarBaja)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkVigencia)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try {
            if (txtCodigoCategoria.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar el código de categoría para buscar",
                        "Campo Vacío",
                        JOptionPane.WARNING_MESSAGE);
                txtCodigoCategoria.requestFocus();
                return;
            }

            // Deshabilitar durante búsqueda
            habilitarBotones("PROCESANDO");

            ResultSet rs = cat.buscarCategoria(Integer.parseInt(txtCodigoCategoria.getText()));

            if (rs.next()) {
                txtCodigoCategoria.setText(String.valueOf(rs.getInt("idcategoria")));
                txtNombreCategoria.setText(rs.getString("nombrecategoria"));
                txtDescripcion.setText(rs.getString("descripcion"));
                checkVigencia.setSelected(rs.getBoolean("estado"));

                // Habilitar botones de acción sobre el registro
                habilitarBotones("CONSULTADO");

                JOptionPane.showMessageDialog(this,
                        "Categoría encontrada",
                        "Búsqueda Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró ninguna categoría con ese código",
                        "No Encontrado",
                        JOptionPane.INFORMATION_MESSAGE);
                habilitarBotones("INICIAL");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El código debe ser un número válido",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
            habilitarBotones("INICIAL");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar la categoría:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            habilitarBotones("INICIAL");
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        try {
            if (btnNuevo.getText().equals("NUEVO")) {
                // Cambiar a modo CREANDO
                habilitarBotones("CREANDO");

                // Se crea un nuevo codigo
                txtCodigoCategoria.setText(String.valueOf(cat.crearCodigoCategoria()));
                btnNuevo.setText("GUARDAR");

                // Dar foco al nombre
                txtNombreCategoria.requestFocus();

            } else {
                // Validar campos antes de insertar
                if (txtNombreCategoria.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(this,
                            "El campo Nombre de Categoría es obligatorio",
                            "Validación",
                            JOptionPane.WARNING_MESSAGE);
                    txtNombreCategoria.requestFocus();
                    return;
                }

                if (txtDescripcion.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(this,
                            "El campo Descripción es obligatorio",
                            "Validación",
                            JOptionPane.WARNING_MESSAGE);
                    txtDescripcion.requestFocus();
                    return;
                }

                // Confirmar inserción
                int opcion = JOptionPane.showConfirmDialog(this,
                        "¿Desea guardar esta nueva categoría?",
                        "Confirmar Inserción",
                        JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {
                    // Deshabilitar todo durante el proceso
                    habilitarBotones("PROCESANDO");

                    cat.insertarCategoria(
                            Integer.parseInt(txtCodigoCategoria.getText()),
                            txtNombreCategoria.getText().trim(),
                            txtDescripcion.getText().trim(),
                            checkVigencia.isSelected()
                    );

                    JOptionPane.showMessageDialog(this,
                            "Categoría registrada exitosamente",
                            "Registro Exitoso",
                            JOptionPane.INFORMATION_MESSAGE);

                    btnNuevo.setText("NUEVO");
                    LimpiarCampos();
                    ListarCategoria();
                    habilitarBotones("INICIAL");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al insertar la categoría:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            btnNuevo.setText("NUEVO");
            habilitarBotones("INICIAL");
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        LimpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        try {
            if (txtCodigoCategoria.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Debe buscar o seleccionar una categoría para eliminar",
                        "Campo Vacío",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar que existe
            ResultSet rs = cat.buscarCategoria(Integer.parseInt(txtCodigoCategoria.getText()));
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "La categoría no existe en el sistema",
                        "Categoría No Encontrada",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String mensaje = "¿Está seguro que desea ELIMINAR esta categoría?\n\n"
                    + "• Si tiene libros asociados: Se desactivará la categoría y sus libros\n"
                    + "• Si NO tiene libros: Se eliminará permanentemente del sistema";

            int opcion = JOptionPane.showConfirmDialog(this,
                    mensaje,
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                // Deshabilitar durante el proceso
                habilitarBotones("PROCESANDO");

                cat.eliminarCategoria(Integer.parseInt(txtCodigoCategoria.getText()));

                JOptionPane.showMessageDialog(this,
                        "Categoría procesada correctamente.\n"
                        + "Si tenía libros asociados, fueron desactivados junto con la categoría.",
                        "Operación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                LimpiarCampos();
                ListarCategoria();
                habilitarBotones("INICIAL");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar la categoría:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            habilitarBotones("CONSULTADO"); // Volver al estado anterior
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {
            if (txtCodigoCategoria.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Debe buscar una categoría primero",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtNombreCategoria.getText().trim().equals("")
                    || txtDescripcion.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Los campos Nombre y Descripción no pueden estar vacíos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String mensaje = "¿Desea actualizar esta categoría?";
            if (!checkVigencia.isSelected()) {
                mensaje = "¿Está seguro de DESACTIVAR esta categoría?\n\n"
                        + "ADVERTENCIA: Si tiene libros asociados, también serán desactivados.";
            }

            int opcion = JOptionPane.showConfirmDialog(this,
                    mensaje,
                    "Confirmar Actualización",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                // Deshabilitar durante el proceso
                habilitarBotones("PROCESANDO");

                cat.ActualizarCategoria(
                        Integer.parseInt(txtCodigoCategoria.getText()),
                        txtNombreCategoria.getText().trim(),
                        txtDescripcion.getText().trim(),
                        checkVigencia.isSelected()
                );

                String mensajeExito = "Categoría actualizada correctamente";
                if (!checkVigencia.isSelected()) {
                    mensajeExito += ".\nLos libros asociados también fueron desactivados.";
                }

                JOptionPane.showMessageDialog(this,
                        mensajeExito,
                        "Actualización Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                LimpiarCampos();
                ListarCategoria();
                habilitarBotones("INICIAL");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar la categoría:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            habilitarBotones("CONSULTADO"); // Volver al estado anterior
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnDarBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarBajaActionPerformed
        try {
            if (txtCodigoCategoria.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Debe buscar o seleccionar una categoría para dar de baja",
                        "Campo Vacío",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar estado actual
            ResultSet rs = cat.buscarCategoria(Integer.parseInt(txtCodigoCategoria.getText()));
            if (rs.next()) {
                boolean estadoActual = rs.getBoolean("estado");

                if (!estadoActual) {
                    JOptionPane.showMessageDialog(this,
                            "Esta categoría ya está INACTIVA.\n"
                            + "No es necesario darla de baja nuevamente.",
                            "Categoría Ya Inactiva",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            String mensaje = "¿Está seguro de DAR DE BAJA esta categoría?\n\n"
                    + "Esta acción:\n"
                    + "• Desactivará la categoría\n"
                    + "• Desactivará TODOS los libros asociados a esta categoría\n\n"
                    + "¿Desea continuar?";

            int opcion = JOptionPane.showConfirmDialog(this,
                    mensaje,
                    "Confirmar Dar de Baja",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                // Deshabilitar durante el proceso
                habilitarBotones("PROCESANDO");

                cat.DarBajaCategoria(Integer.parseInt(txtCodigoCategoria.getText()));

                JOptionPane.showMessageDialog(this,
                        "Categoría dada de baja exitosamente.\n"
                        + "Los libros asociados también fueron desactivados.",
                        "Operación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                LimpiarCampos();
                ListarCategoria();
                habilitarBotones("INICIAL");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al dar de baja la categoría:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            habilitarBotones("CONSULTADO"); // Volver al estado anterior
        }
    }//GEN-LAST:event_btnDarBajaActionPerformed

    private void tblCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategoriaMouseClicked
        // Obtener el código de la fila seleccionada
        int filaSeleccionada = tblCategoria.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtCodigoCategoria.setText(String.valueOf(tblCategoria.getValueAt(filaSeleccionada, 0)));
            btnBuscarActionPerformed(null);
        }
    }//GEN-LAST:event_tblCategoriaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnDarBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox checkVigencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblCategoria;
    private javax.swing.JTextField txtCodigoCategoria;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombreCategoria;
    // End of variables declaration//GEN-END:variables
}
