package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class VistaParametros extends JFrame {

    private final Color NARANJA_PRINCIPAL = new Color(255, 107, 0);
    private final Color NARANJA_DEGRADADO = new Color(255, 145, 56);
    private final Color AZUL_OSCURO = new Color(15, 23, 42);
    private final Color FONDO_APP = new Color(244, 246, 249);
    private final Color TEXTO_OSCURO = new Color(30, 41, 59);

    private JTextField txtNombreParametro;
    private JTextField txtPuntajeMaximo;
    private JTable tblParametros;

    public VistaParametros() {
        setTitle("ERP Papelería | Parámetros de Evaluación");
        setSize(900, 560);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        construirInterfaz();
        // Cargar los criterios automáticamente al abrir la ventana
        ParametroServicio.cargarTablaParametros(tblParametros);
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
        header.setPreferredSize(new Dimension(900, 58));
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        JLabel lblTitulo = new JLabel("SELECCIÓN  |  RÚBRICAS Y PARÁMETROS DE EVALUACIÓN");
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

        // 2. Contenedor Principal
        JPanel contenedor = new JPanel(new BorderLayout(0, 20));
        contenedor.setBackground(FONDO_APP);
        contenedor.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        // Tarjeta Superior: Formulario
        JPanel cardForm = crearTarjetaBlanca();
        cardForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblSub = new JLabel("Nuevo Criterio de Calificación");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSub.setForeground(TEXTO_OSCURO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        cardForm.add(lblSub, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0; cardForm.add(crearEtiqueta("Nombre del Parámetro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        txtNombreParametro = crearCampoTexto();
        cardForm.add(txtNombreParametro, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        cardForm.add(crearEtiqueta("Puntaje Máximo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtPuntajeMaximo = crearCampoTexto();
        cardForm.add(txtPuntajeMaximo, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        JButton btnRefrescar = new JButton("Actualizar Lista");
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefrescar.setForeground(TEXTO_OSCURO);
        btnRefrescar.setBackground(new Color(226, 232, 240));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> ParametroServicio.cargarTablaParametros(tblParametros));

        JButton btnGuardar = new JButton("Guardar Parámetro");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(NARANJA_PRINCIPAL);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnGuardar.setBackground(NARANJA_DEGRADADO); }
            @Override
            public void mouseExited(MouseEvent e) { btnGuardar.setBackground(NARANJA_PRINCIPAL); }
        });
        btnGuardar.addActionListener(e -> guardarParametro());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4;
        cardForm.add(panelBotones, gbc);

        contenedor.add(cardForm, BorderLayout.NORTH);

        // Tarjeta Inferior: Tabla
        JPanel cardTabla = crearTarjetaBlanca();
        cardTabla.setLayout(new BorderLayout(0, 10));

        JLabel lblLista = new JLabel("Criterios Activos en el Sistema");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLista.setForeground(TEXTO_OSCURO);
        cardTabla.add(lblLista, BorderLayout.NORTH);

        tblParametros = new JTable();
        tblParametros.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblParametros.setRowHeight(30);
        tblParametros.setGridColor(new Color(241, 245, 249));
        tblParametros.setSelectionBackground(new Color(255, 237, 213));

        JTableHeader th = tblParametros.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(AZUL_OSCURO);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 34));

        JScrollPane scroll = new JScrollPane(tblParametros);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);
        cardTabla.add(scroll, BorderLayout.CENTER);

        contenedor.add(cardTabla, BorderLayout.CENTER);
        add(contenedor, BorderLayout.CENTER);
    }

    private void guardarParametro() {
        String nombre = txtNombreParametro.getText().trim();
        String puntaje = txtPuntajeMaximo.getText().trim();

        if (nombre.isEmpty() || puntaje.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete ambos campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ParametroServicio.insertarParametro(nombre, puntaje)) {
            JOptionPane.showMessageDialog(this, "¡Parámetro registrado con éxito!");
            txtNombreParametro.setText("");
            txtPuntajeMaximo.setText("");
            ParametroServicio.cargarTablaParametros(tblParametros);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaParametros().setVisible(true));
    }
}
