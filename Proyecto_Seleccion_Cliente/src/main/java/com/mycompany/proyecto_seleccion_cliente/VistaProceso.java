package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class VistaProceso extends JFrame {

    private final Color NARANJA_PRINCIPAL = new Color(255, 107, 0);
    private final Color NARANJA_DEGRADADO = new Color(255, 145, 56);
    private final Color AZUL_OSCURO = new Color(15, 23, 42);
    private final Color VERDE_CONTRATAR = new Color(16, 185, 129);
    private final Color FONDO_APP = new Color(244, 246, 249);

    private JComboBox<String> cmbCandidato;
    private JTextField txtFecha;
    private JComboBox<String> cmbParametro;
    private JTextField txtCalificacion;
    private JTable tblDetalle;
    private DefaultTableModel modeloDetalle;

    public VistaProceso() {
        setTitle("ERP Papelería | Procesos de Evaluación y Contratación");
        setSize(960, 640);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        construirInterfaz();

        // Inicializar datos desde Oracle
        ProcesoServicio.cargarComboCandidatos(cmbCandidato);
        ProcesoServicio.cargarComboParametros(cmbParametro);
        txtFecha.setText(LocalDate.now().toString());
    }

    private void construirInterfaz() {
        // Header Naranja
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, NARANJA_PRINCIPAL, getWidth(), 0, NARANJA_DEGRADADO));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(960, 58));
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        JLabel lblTitulo = new JLabel("SELECCIÓN  |  EVALUACIÓN DE CANDIDATOS E INTEGRACIÓN");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnCerrar = new JButton("Volver al Menú");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(AZUL_OSCURO);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnCerrar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Contenedor Principal
        JPanel contenedor = new JPanel(new BorderLayout(0, 18));
        contenedor.setBackground(FONDO_APP);
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 30, 25, 30));

        // 1. Tarjeta Cabecera
        JPanel cardCabecera = crearTarjetaBlanca();
        cardCabecera.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblSec1 = new JLabel("1. Datos Generales de la Evaluación (Cabecera)");
        lblSec1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSec1.setForeground(NARANJA_PRINCIPAL);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        cardCabecera.add(lblSec1, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0; cardCabecera.add(crearEtiqueta("Postulante a Evaluar:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        cmbCandidato = new JComboBox<>();
        cmbCandidato.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCandidato.setPreferredSize(new Dimension(250, 34));
        cardCabecera.add(cmbCandidato, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        cardCabecera.add(crearEtiqueta("Fecha de Registro:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtFecha = crearCampoTexto();
        txtFecha.setEditable(false);
        txtFecha.setBackground(new Color(241, 245, 249));
        cardCabecera.add(txtFecha, gbc);

        contenedor.add(cardCabecera, BorderLayout.NORTH);

        // 2. Tarjeta Detalle y Tabla
        JPanel cardDetalle = crearTarjetaBlanca();
        cardDetalle.setLayout(new BorderLayout(0, 15));

        JPanel panelControlesDetalle = new JPanel(new GridBagLayout());
        panelControlesDetalle.setOpaque(false);
        GridBagConstraints gbcD = new GridBagConstraints();
        gbcD.insets = new Insets(5, 8, 5, 8);
        gbcD.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblSec2 = new JLabel("2. Asignación de Puntajes por Rúbrica (Detalle)");
        lblSec2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSec2.setForeground(NARANJA_PRINCIPAL);
        gbcD.gridx = 0; gbcD.gridy = 0; gbcD.gridwidth = 5;
        panelControlesDetalle.add(lblSec2, gbcD);

        gbcD.gridwidth = 1; gbcD.gridy = 1;
        gbcD.gridx = 0; panelControlesDetalle.add(crearEtiqueta("Parámetro:"), gbcD);
        gbcD.gridx = 1; gbcD.weightx = 0.6;
        cmbParametro = new JComboBox<>();
        cmbParametro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbParametro.setPreferredSize(new Dimension(260, 34));
        panelControlesDetalle.add(cmbParametro, gbcD);

        gbcD.gridx = 2; gbcD.weightx = 0;
        panelControlesDetalle.add(crearEtiqueta("Nota:"), gbcD);
        gbcD.gridx = 3; gbcD.weightx = 0.2;
        txtCalificacion = crearCampoTexto();
        panelControlesDetalle.add(txtCalificacion, gbcD);

        gbcD.gridx = 4; gbcD.weightx = 0.2;
        JButton btnAgregar = new JButton("+ Agregar Línea");
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBackground(AZUL_OSCURO);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(9, 15, 9, 15));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarLineaDetalle());
        panelControlesDetalle.add(btnAgregar, gbcD);

        cardDetalle.add(panelControlesDetalle, BorderLayout.NORTH);

        // Tabla Detalle
        modeloDetalle = new DefaultTableModel();
        modeloDetalle.addColumn("Cód. Parámetro");
        modeloDetalle.addColumn("Parámetro Evaluado");
        modeloDetalle.addColumn("Calificación");

        tblDetalle = new JTable(modeloDetalle);
        tblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblDetalle.setRowHeight(28);
        JTableHeader th = tblDetalle.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(AZUL_OSCURO);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 32));

        JScrollPane scroll = new JScrollPane(tblDetalle);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);
        cardDetalle.add(scroll, BorderLayout.CENTER);

        contenedor.add(cardDetalle, BorderLayout.CENTER);

        // 3. Barra Inferior de Acciones (Guardar Evaluación e Integración Contratar)
        JPanel barraAcciones = new JPanel(new BorderLayout());
        barraAcciones.setOpaque(false);

        JButton btnContratar = new JButton("Contratar N Mejores (Integración Nómina)");
        btnContratar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnContratar.setForeground(Color.WHITE);
        btnContratar.setBackground(VERDE_CONTRATAR);
        btnContratar.setFocusPainted(false);
        btnContratar.setBorder(BorderFactory.createEmptyBorder(11, 20, 11, 20));
        btnContratar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContratar.addActionListener(e -> ejecutarContratacion());

        JButton btnGuardarEval = new JButton("Guardar Evaluación Completa");
        btnGuardarEval.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardarEval.setForeground(Color.WHITE);
        btnGuardarEval.setBackground(NARANJA_PRINCIPAL);
        btnGuardarEval.setFocusPainted(false);
        btnGuardarEval.setBorder(BorderFactory.createEmptyBorder(11, 25, 11, 25));
        btnGuardarEval.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardarEval.addActionListener(e -> guardarEvaluacion());

        barraAcciones.add(btnContratar, BorderLayout.WEST);
        barraAcciones.add(btnGuardarEval, BorderLayout.EAST);

        contenedor.add(barraAcciones, BorderLayout.SOUTH);
        add(contenedor, BorderLayout.CENTER);
    }

    private void agregarLineaDetalle() {
        if (cmbParametro.getSelectedItem() == null || txtCalificacion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un parámetro y escriba una calificación.");
            return;
        }
        String seleccionado = cmbParametro.getSelectedItem().toString();
        String[] partes = seleccionado.split(" - ");
        modeloDetalle.addRow(new Object[]{partes[0], partes[1], txtCalificacion.getText().trim()});
        txtCalificacion.setText("");
    }

    private void guardarEvaluacion() {
        if (cmbCandidato.getSelectedItem() == null || tblDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un candidato y agregue al menos una calificación.");
            return;
        }
String cedula = cmbCandidato.getSelectedItem().toString().split(" - ")[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tblDetalle.getRowCount(); i++) {
sb.append(tblDetalle.getValueAt(i, 0)).append(",").append(tblDetalle.getValueAt(i, 2));
if (i < tblDetalle.getRowCount() - 1) sb.append(";");
}
if (ProcesoServicio.registrarEvaluacion(cedula, sb.toString())) {
JOptionPane.showMessageDialog(this, "¡Evaluación guardada exitosamente en Oracle!");
modeloDetalle.setRowCount(0);
}
}
private void ejecutarContratacion() {
String cantStr = JOptionPane.showInputDialog(this, "¿Cuántos mejores candidatos desea contratar?");
if (cantStr == null || cantStr.trim().isEmpty()) return;
String sueldoStr = JOptionPane.showInputDialog(this, "Ingrese el sueldo base para los contratados:");
if (sueldoStr == null || sueldoStr.trim().isEmpty()) return;
try {
int cantidad = Integer.parseInt(cantStr.trim());
double sueldo = Double.parseDouble(sueldoStr.trim());
if (ProcesoServicio.ejecutarContratacion(cantidad, sueldo)) {
JOptionPane.showMessageDialog(this, "¡Contratación ejecutada! Los " + cantidad + " mejores pasaron a Nómina.");
}
} catch (NumberFormatException ex) {
JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
}
}
private JPanel crearTarjetaBlanca() {
JPanel panel = new JPanel() {
@Override
protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g;
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2.setColor(Color.WHITE);
g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
g2.setColor(new Color(226, 232, 240));
g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
}
};
panel.setOpaque(false);
panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
return panel;
}
private JLabel crearEtiqueta(String texto) {
JLabel lbl = new JLabel(texto);
lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
lbl.setForeground(new Color(71, 85, 105));
return lbl;
}
private JTextField crearCampoTexto() {
JTextField txt = new JTextField();
txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
txt.setPreferredSize(new Dimension(140, 34));
txt.setBorder(BorderFactory.createCompoundBorder(
BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
BorderFactory.createEmptyBorder(4, 10, 4, 10)
));
return txt;
}
public static void main(String[] args) {
SwingUtilities.invokeLater(() -> new VistaProceso().setVisible(true));
}
}