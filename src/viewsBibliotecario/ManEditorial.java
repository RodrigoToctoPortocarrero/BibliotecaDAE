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
 * @author Valentino Lopez
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

            // Validación de Código (ID)
            if (codigoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo Código es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            int id = Integer.parseInt(codigoStr);
            editorial.setIdeditorial(id);

            // Obtenemos los textos para validar
            String nombre = txtnombre.getText().trim();
            String telefono = txttelefono.getText().trim();
            String correo = txtcorreo.getText().trim();

            // 1. Validar campos vacíos
            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos de texto son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // 2. VALIDACIÓN DE TELÉFONO (9 dígitos y solo números)
            if (!telefono.matches("\\d{9}")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe tener exactamente 9 dígitos numéricos.", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                return null; // Detenemos el proceso
            }

            // 3. VALIDACIÓN DE CORREO (@ y .com)
            // La expresión regular verifica que tenga texto + @ + texto + .com
            if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") || !correo.contains(".com")) {
                JOptionPane.showMessageDialog(this, "El correo debe ser válido (ejemplo@dominio.com).", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                return null; // Detenemos el proceso
            }

            // Si pasa todas las validaciones, llenamos el objeto
            editorial.setNombre(nombre);
            editorial.setTelefono(telefono);
            editorial.setCorreo(correo);
            editorial.setEstado(cbxVigencia.isSelected());

            return editorial;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El Código debe ser un número entero válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Correo:");

        btnlimpiar.setBackground(new java.awt.Color(24, 118, 210));
        btnlimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnlimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnlimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/limpiarMarca.png"))); // NOI18N
        btnlimpiar.setText("LIMPIAR");
        btnlimpiar.setBorder(null);
        btnlimpiar.setBorderPainted(false);
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });

        btneliminar.setBackground(new java.awt.Color(24, 118, 210));
        btneliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(255, 255, 255));
        btneliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminarMarca.png"))); // NOI18N
        btneliminar.setText("ELIMINAR");
        btneliminar.setBorder(null);
        btneliminar.setBorderPainted(false);
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        btnnuevo.setBackground(new java.awt.Color(24, 118, 210));
        btnnuevo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnnuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnnuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/registrarMarca.png"))); // NOI18N
        btnnuevo.setText("NUEVO");
        btnnuevo.setBorder(null);
        btnnuevo.setBorderPainted(false);
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });

        btnmodificar.setBackground(new java.awt.Color(24, 118, 210));
        btnmodificar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnmodificar.setForeground(new java.awt.Color(255, 255, 255));
        btnmodificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/modificarMarca.png"))); // NOI18N
        btnmodificar.setText("MODIFICAR");
        btnmodificar.setBorder(null);
        btnmodificar.setBorderPainted(false);
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });

        btndarbaja.setBackground(new java.awt.Color(24, 118, 210));
        btndarbaja.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btndarbaja.setForeground(new java.awt.Color(255, 255, 255));
        btndarbaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/darBajaMarca.png"))); // NOI18N
        btndarbaja.setText("DAR BAJA");
        btndarbaja.setBorder(null);
        btndarbaja.setBorderPainted(false);
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
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnnuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnlimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnmodificar, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(btndarbaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(73, 73, 73))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbxVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(68, 68, 68)
                                    .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtcorreo))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(148, 148, 148))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnnuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cbxVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btndarbaja, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnlimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(codigoStr);


        if (objEditorial.tieneLibrosActivos(id)) {
            JOptionPane.showMessageDialog(this, 
                "No se puede ELIMINAR esta editorial.\nTiene libros ACTIVOS asociados en el sistema.", 
                "Operación Denegada", 
                JOptionPane.ERROR_MESSAGE);
            return; // Detenemos la operación aquí
        }
        // ----------------------------------------------------

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¡ADVERTENCIA! ¿Desea ELIMINAR PERMANENTEMENTE la Editorial: " + txtnombre.getText() + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                objEditorial.eliminar(id);
                JOptionPane.showMessageDialog(this, "Editorial eliminada permanentemente.", "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTabla();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar (Posiblemente libros inactivos históricos asociados): " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        try {
            // FASE 1: Preparar para la inserción (Botón dice NUEVO)
            if (btnnuevo.getText().equals("NUEVO")) {
                limpiarCampos();
                
                // --- NUEVO: Generar ID Automático ---
                // Llamamos a la función que agregaste en la capa lógica
                int nuevoId = objEditorial.generarSiguienteId();
                txtcodigo.setText(String.valueOf(nuevoId));
                
                // Bloqueamos el campo para que nadie lo cambie y rompa la secuencia
                txtcodigo.setEditable(false);
                txtcodigo.setEnabled(false);
                
                // Cambiamos el texto del botón
                btnnuevo.setText("GUARDAR");

            // FASE 2: Insertar la Editorial (Botón dice GUARDAR)
            } else {

                // 1. Validar campos obligatorios (Nombre, Teléfono, Correo)
                // Ya no validamos el código porque se generó solo
                if (txtnombre.getText().trim().isEmpty()
                        || txttelefono.getText().trim().isEmpty()
                        || txtcorreo.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. Obtener el objeto Editorial
                // Como el campo código está deshabilitado, debemos asegurarnos de leerlo bien
                // OJO: Tu método 'obtenerEditorialDesdeCampos' intenta leer txtcodigo.
                // Como ya tiene el número puesto, funcionará bien.
                Editorial nuevaEditorial = obtenerEditorialDesdeCampos();
                
                if (nuevaEditorial == null) {
                    return;
                }

                // 3. Verificación de duplicados (Seguridad extra)
                // Aunque generamos el ID, si alguien más insertó justo ahora, esto nos avisa.
                if (objEditorial.buscarPorCodigo(nuevaEditorial.getIdeditorial()).next()) {
                    JOptionPane.showMessageDialog(this, "El ID " + nuevaEditorial.getIdeditorial() + " ya fue ocupado. Intente de nuevo.", "Error de Concurrencia", JOptionPane.ERROR_MESSAGE);
                    // Si falla, reiniciamos el proceso para que genere el siguiente ID
                    btnnuevo.setText("NUEVO");
                    btnnuevo.doClick(); 
                    return;
                }

                // 4. Insertar en BD
                objEditorial.registrar(nuevaEditorial);

                JOptionPane.showMessageDialog(this, "Editorial registrada con éxito (ID: " + nuevaEditorial.getIdeditorial() + ").", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

                // 5. Resetear formulario
                btnnuevo.setText("NUEVO");
                limpiarCampos();
                cargarTabla();
                
                // Restaurar estado normal de los campos
                txtcodigo.setEditable(true);
                txtcodigo.setEnabled(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
            // Si hubo error, restaurar botón
            btnnuevo.setText("NUEVO");
            txtcodigo.setEnabled(true);
            txtcodigo.setEditable(true);
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
        
        int id = Integer.parseInt(codigoStr);

        if (objEditorial.tieneLibrosActivos(id)) {
            JOptionPane.showMessageDialog(this, 
                "No se puede dar de BAJA esta editorial.\nTiene libros ACTIVOS asociados. Primero dé de baja los libros.", 
                "Operación Denegada", 
                JOptionPane.ERROR_MESSAGE);
            return; // Detenemos la operación aquí
        }
        // ----------------------------------------------------
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Desea dar de BAJA la Editorial: " + txtnombre.getText() + "? (Estado = No Vigente)", 
                "Confirmar Baja", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
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
