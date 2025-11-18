package viewsBibliotecario;


import capaLogica.Editorial; 
import java.sql.ResultSet; 
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author laboratorio_computo
 */
public class ManEditorial extends javax.swing.JPanel {

   Editorial objEditorial = new Editorial();
   DefaultTableModel modeloTabla = new DefaultTableModel();

   
    /**
     * Creates new form ManEditorial
     */
    public ManEditorial() {
    initComponents();
    tblEditorial.setFillsViewportHeight(false); 
    inicializarTabla();
    cargarTabla();
  
    
    int anchoFijo = 800;  // EJEMPLO: Ajusta este ancho
    int altoFijo = 600;   // EJEMPLO: Ajusta esta altura
    this.setPreferredSize(new java.awt.Dimension(anchoFijo, altoFijo));
    this.setMinimumSize(new java.awt.Dimension(anchoFijo, altoFijo));
    Border bordeExterior = BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1);
    Border bordeInterior = new EmptyBorder(2, 2, 2, 2);
    Border bordeFinal = new CompoundBorder(bordeExterior, bordeInterior);
    jScrollPane1.setBorder(bordeFinal); 
    tblEditorial.setBorder(null);
    tblEditorial.setFillsViewportHeight(false); 
    
    }
    

    private void inicializarTabla() {
        String[] titulos = {"Código", "Nombre", "Teléfono", "Correo", "Vigencia"};
        modeloTabla.setColumnIdentifiers(titulos);
        tblEditorial.setModel(modeloTabla);

        // --- 🚨 AJUSTE DE ANCHO DE COLUMNAS (NUEVO CÓDIGO) 🚨 ---
        // 1. Obtener el modelo de columnas
        // JTable.getColumnModel() devuelve la estructura de las columnas.
        TableColumn columna = null;

        // 2. Establecer el ancho de las columnas (en píxeles)
        // Columna 0: Código (Ancho pequeño, 70px)
        columna = tblEditorial.getColumnModel().getColumn(0);
        columna.setPreferredWidth(70);
        columna.setResizable(false); // Opcional: El usuario no puede arrastrar el ancho

        // Columna 1: Nombre (Ancho mediano, 150px)
        columna = tblEditorial.getColumnModel().getColumn(1);
        columna.setPreferredWidth(180);

        // Columna 2: Teléfono (Ancho pequeño, 100px)
        columna = tblEditorial.getColumnModel().getColumn(2);
        columna.setPreferredWidth(100);

        // Columna 3: Correo (Ancho amplio, 200px) 👈 TU SOLICITUD
        columna = tblEditorial.getColumnModel().getColumn(3);
        columna.setPreferredWidth(200);

        // Columna 4: Vigencia (Ancho pequeño, 90px)
        columna = tblEditorial.getColumnModel().getColumn(4);
        columna.setPreferredWidth(90);
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0); // Limpiar filas anteriores
        ResultSet rs = null;

        try {
            rs = objEditorial.listarEditorial();

            while (rs.next()) {
                // Usa nombres de columna de la BD
                int id = rs.getInt("ideditorial");
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                boolean estado = rs.getBoolean("estado");

                String vigencia = estado ? "Vigente" : "No Vigente";

                Object[] fila = {id, nombre, telefono, correo, vigencia};
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            // Si hay un error aquí, la tabla estará vacía y verás la ventana de error.
            JOptionPane.showMessageDialog(this, "Error al cargar la tabla: " + e.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
        txtcodigo.setText("");
        txtnombre.setText("");
        txttelefono.setText("");
        txtcorreo.setText("");
        cbxVigencia.setSelected(false);
        txtcodigo.setEnabled(true); 
        btnBuscar.setEnabled(true);
    }
    
    // Obtiene los datos de la interfaz y los asigna al objeto Editorial
   
    private Editorial obtenerEditorialDesdeCampos() {
        Editorial editorial = new Editorial();

        try {
            String codigoStr = txtcodigo.getText().trim();

            // 🚨 CRÍTICO: Si la cadena está vacía, no se intenta nada y se detiene.
            if (codigoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo Código es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Asignar el ID solo después de verificar que no está vacío
            int id = Integer.parseInt(codigoStr);
            editorial.setIdeditorial(id);

            // (Validaciones de otros campos... se asume que pasan o están correctamente implementadas)
            if (txtnombre.getText().trim().isEmpty()
                    || txttelefono.getText().trim().isEmpty()
                    || txtcorreo.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Todos los campos de texto son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            editorial.setNombre(txtnombre.getText().trim());
            editorial.setTelefono(txttelefono.getText().trim());
            editorial.setCorreo(txtcorreo.getText().trim());
            editorial.setEstado(cbxVigencia.isSelected());

            return editorial;

        } catch (NumberFormatException e) {
            // Captura si el usuario puso letras en el campo de código
            JOptionPane.showMessageDialog(this, "El Código debe ser un número entero válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            // Para cualquier otro error durante la captura de datos
            JOptionPane.showMessageDialog(this, "Error al obtener datos de los campos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtcodigo = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtcorreo = new javax.swing.JTextField();
        btnlimpiar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnnuevo = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        btndarbaja = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEditorial = new javax.swing.JTable();
        txttelefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbxVigencia = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

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

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Código:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Nombre:");

        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscarMarca.png"))); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Correo:");

        btnlimpiar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnlimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/limpiarMarca.png"))); // NOI18N
        btnlimpiar.setText("LIMPIAR");
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });

        btneliminar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btneliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminarMarca.png"))); // NOI18N
        btneliminar.setText("ELIMINAR");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        btnnuevo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnnuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/registrarMarca.png"))); // NOI18N
        btnnuevo.setText("NUEVO");
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });

        btnmodificar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnmodificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/modificarMarca.png"))); // NOI18N
        btnmodificar.setText("MODIFICAR");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });

        btndarbaja.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btndarbaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/darBajaMarca.png"))); // NOI18N
        btndarbaja.setText("DAR BAJA");
        btndarbaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndarbajaActionPerformed(evt);
            }
        });

        tblEditorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Telefono", "Correo", "VIgencia"
            }
        ));
        tblEditorial.setMaximumSize(new java.awt.Dimension(600, 250));
        tblEditorial.setPreferredSize(new java.awt.Dimension(600, 250));
        tblEditorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEditorialMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEditorial);
        if (tblEditorial.getColumnModel().getColumnCount() > 0) {
            tblEditorial.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Telefono:");

        cbxVigencia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cbxVigencia.setText("Vigente");
        cbxVigencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxVigenciaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Vigencia:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btnnuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnlimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btndarbaja, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnmodificar, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(68, 68, 68)
                                        .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8)
                                        .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(btnBuscar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(cbxVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnnuevo)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btneliminar)
                        .addComponent(btnlimpiar))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btndarbaja)
                        .addComponent(btnmodificar)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
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
        String codigoStr = txtcodigo.getText().trim();
        if (codigoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ResultSet rs = null;

        try {
            int id = Integer.parseInt(codigoStr);
            // Usamos el método que devuelve ResultSet
            rs = objEditorial.buscarPorCodigo(id);

            if (rs.next()) {
                // Rellenar campos usando los datos del ResultSet
                txtnombre.setText(rs.getString("nombre"));
                txttelefono.setText(rs.getString("telefono"));
                txtcorreo.setText(rs.getString("correo"));
                cbxVigencia.setSelected(rs.getBoolean("estado"));

                txtcodigo.setEnabled(false);
                btnBuscar.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Editorial encontrada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                limpiarCampos();
                txtcodigo.setText(codigoStr); // Mantiene el código ingresado
                JOptionPane.showMessageDialog(this, "Editorial con código " + id + " no encontrada.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El Código debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de búsqueda: " + e.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
    
        limpiarCampos();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        String codigoStr = txtcodigo.getText().trim();
        if (codigoStr.isEmpty() || txtnombre.getText().trim().isEmpty() || txtcodigo.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para eliminar permanentemente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¡ADVERTENCIA! ¿Desea ELIMINAR PERMANENTEMENTE la Editorial: " + txtnombre.getText() + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(codigoStr);
                objEditorial.eliminar(id);
                JOptionPane.showMessageDialog(this, "Editorial eliminada permanentemente.", "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTabla();
            } catch (Exception e) {
                // Aquí capturamos errores de FK si hay libros asociados
                JOptionPane.showMessageDialog(this, "Error al eliminar. Verifique que no haya libros asociados: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        }  
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        try {
            // FASE 1: Preparar para la inserción (Botón dice NUEVO)
            if (btnnuevo.getText().equals("NUEVO")) {
                limpiarCampos();
                txtcodigo.setEnabled(true); // Permitir que el usuario ingrese el ID
                btnnuevo.setText("GUARDAR");

                // FASE 2: Insertar la Editorial (Botón dice GUARDAR)
            } else {

                // 🚨 1. VALIDACIÓN CRÍTICA DEL CÓDIGO Y OTROS CAMPOS (Evita Error NULL)
                String codigoStr = txtcodigo.getText().trim();

                if (codigoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar un Código (ID) para la Editorial antes de guardar.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que sea un número (para evitar NumberFormatException)
                int idEditorial;
                try {
                    idEditorial = Integer.parseInt(codigoStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El Código debe ser un número entero válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar campos de texto restantes
                if (txtnombre.getText().trim().isEmpty()
                        || txttelefono.getText().trim().isEmpty()
                        || txtcorreo.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Nombre, Teléfono y Correo son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. Obtener el objeto Editorial (ya validado)
                Editorial nuevaEditorial = obtenerEditorialDesdeCampos();
                if (nuevaEditorial == null) {
                    return;
                }

                // 3. Verificación de existencia (Maneja el error de "Ya existe...")
                // Usamos .next() para ver si el ResultSet tiene datos.
                if (objEditorial.buscarPorCodigo(nuevaEditorial.getIdeditorial()).next()) {
                    JOptionPane.showMessageDialog(this, "Ya existe una Editorial con este Código (" + nuevaEditorial.getIdeditorial() + "). Debe ingresar un ID diferente.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 4. Insertamos
                objEditorial.registrar(nuevaEditorial);

                JOptionPane.showMessageDialog(this, "Editorial registrada con éxito.", "Registro", JOptionPane.INFORMATION_MESSAGE);

                // 5. Volver a la normalidad
                btnnuevo.setText("NUEVO");
                limpiarCampos();
                cargarTabla();
            }
        } catch (Exception e) {
            // Capturamos cualquier error de BD restante.
            JOptionPane.showMessageDialog(this, "Error al insertar Editorial: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);

            // Dejar el botón en modo NUEVO después de la falla.
            btnnuevo.setText("NUEVO");
            txtcodigo.setEnabled(true);
        }
    }//GEN-LAST:event_btnnuevoActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
          Editorial editorialModificada = obtenerEditorialDesdeCampos();
        if (editorialModificada == null) return; 
        
        try {
            objEditorial.modificar(editorialModificada);
            JOptionPane.showMessageDialog(this, "Editorial modificada con éxito.", "Modificación", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btndarbajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndarbajaActionPerformed
         String codigoStr = txtcodigo.getText().trim();
        if (codigoStr.isEmpty() || txtnombre.getText().trim().isEmpty() || txtcodigo.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para dar de baja.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Desea dar de BAJA la Editorial: " + txtnombre.getText() + "? (Estado = No Vigente)", 
                "Confirmar Baja", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(codigoStr);
                objEditorial.darBaja(id);
                JOptionPane.showMessageDialog(this, "Editorial dada de BAJA con éxito.", "Baja Exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTabla();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al dar de baja: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btndarbajaActionPerformed

    private void tblEditorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEditorialMouseClicked
    int fila = tblEditorial.getSelectedRow();
        if (fila == -1) return;
        
        // Obtener datos de la fila
        String codigoStr = modeloTabla.getValueAt(fila, 0).toString();
        String nombre = modeloTabla.getValueAt(fila, 1).toString();
        String telefono = modeloTabla.getValueAt(fila, 2).toString();
        String correo = modeloTabla.getValueAt(fila, 3).toString();
        String vigenciaStr = modeloTabla.getValueAt(fila, 4).toString();
        
        // Rellenar campos de texto
        txtcodigo.setText(codigoStr);
        txtnombre.setText(nombre);
        txttelefono.setText(telefono);
        txtcorreo.setText(correo);
        
        // Rellenar CheckBox
        cbxVigencia.setSelected(vigenciaStr.equals("Vigente"));
        
        // Deshabilitar código para Modificar o Dar Baja
        txtcodigo.setEnabled(false);
        btnBuscar.setEnabled(false);
    }//GEN-LAST:event_tblEditorialMouseClicked

    private void cbxVigenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVigenciaActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cbxVigenciaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btndarbaja;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JCheckBox cbxVigencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tblEditorial;
    private javax.swing.JTextField txtcodigo;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
