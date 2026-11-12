package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class VistaCandidatos extends JFrame {

    private final Color NARANJA_PRINCIPAL = new Color(255, 107, 0);
    private final Color NARANJA_DEGRADADO = new Color(255, 145, 56);
    private final Color AZUL_OSCURO = new Color(15, 23, 42);
    private final Color FONDO_APP = new Color(244, 246, 249);
    private final Color TEXTO_OSCURO = new Color(30, 41, 59);

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtFecha;
    private JTable tblCandidatos;

    public VistaCandidatos() {
        setTitle("ERP Papelería | Gestión de Candidatos");
        setSize(920, 580);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        construirInterfaz();
        // Cargar datos automáticamente desde Oracle al abrir la ventana
        CandidatoServicio.cargarTablaCandidatos(tblCandidatos);
    }

    private void construirInterfaz() {
        // 1. Header Naranja con Degradado
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
        header.setPreferredSize(new Dimension(920, 58));
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        JLabel lblTitulo = new JLabel("SELECCIÓN  |  GESTIÓN DE CANDIDATOS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnCerrar = crearBotonHeader("Volver al Menú");
        btnCerrar.addActionListener(e -> dispose());

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnCerrar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // 2. Contenedor Principal
        JPanel contenedor = new JPanel(new BorderLayout(0, 20));
        contenedor.setBackground(FONDO_APP);
        contenedor.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        // Tarjeta Superior: Formulario de Registro
        JPanel cardForm = crearTarjetaBlanca();
        cardForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblSub = new JLabel("Registro de Nuevo Postulante");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSub.setForeground(TEXTO_OSCURO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        cardForm.add(lblSub, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0; cardForm.add(crearEtiqueta("Cédula (10 dígitos):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        txtCedula = crearCampoTexto();
        cardForm.add(txtCedula, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        cardForm.add(crearEtiqueta("Nombre Completo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7;
        txtNombre = crearCampoTexto();
        cardForm.add(txtNombre, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0; gbc.weightx = 0;
        cardForm.add(crearEtiqueta("Fecha Nac. (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        txtFecha = crearCampoTexto();
        cardForm.add(txtFecha, gbc);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        JButton btnRefrescar = crearBotonSecundario("Actualizar Lista");
        btnRefrescar.addActionListener(e -> CandidatoServicio.cargarTablaCandidatos(tblCandidatos));

        JButton btnGuardar = crearBotonPrimario("Guardar Candidato");
        btnGuardar.addActionListener(e -> guardarCandidato());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnGuardar);

        gbc.gridx = 2; gbc.gridwidth = 2;
        cardForm.add(panelBotones, gbc);

        contenedor.add(cardForm, BorderLayout.NORTH);

        // Tarjeta Inferior: Tabla Estilizada
        JPanel cardTabla = crearTarjetaBlanca();
        cardTabla.setLayout(new BorderLayout(0, 10));

        JLabel lblLista = new JLabel("Listado Oficial de Candidatos Registrados");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLista.setForeground(TEXTO_OSCURO);
        cardTabla.add(lblLista, BorderLayout.NORTH);

        tblCandidatos = new JTable();
        estilizarTabla(tblCandidatos);
        JScrollPane scroll = new JScrollPane(tblCandidatos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);
        cardTabla.add(scroll, BorderLayout.CENTER);

        contenedor.add(cardTabla, BorderLayout.CENTER);
        add(contenedor, BorderLayout.CENTER);
    }

    private void guardarCandidato() {
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String fecha = txtFecha.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (CandidatoServicio.insertarCandidato(cedula, nombre, fecha)) {
            JOptionPane.showMessageDialog(this, "¡Candidato registrado exitosamente!");
            txtCedula.setText("");
            txtNombre.setText("");
            txtFecha.setText("");
            CandidatoServicio.cargarTablaCandidatos(tblCandidatos);
        }
    }

    // --- Componentes Visuales Reutilizables ---
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
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
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
        txt.setPreferredSize(new Dimension(180, 34));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return txt;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(NARANJA_PRINCIPAL);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(NARANJA_DEGRADADO); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(NARANJA_PRINCIPAL); }
        });
        return btn;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(TEXTO_OSCURO);
        btn.setBackground(new Color(226, 232, 240));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonHeader(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_OSCURO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.setGridColor(new Color(241, 245, 249));
        tabla.setSelectionBackground(new Color(255, 237, 213));
        tabla.setSelectionForeground(TEXTO_OSCURO);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(AZUL_OSCURO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 34));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaCandidatos().setVisible(true));
    }
}
