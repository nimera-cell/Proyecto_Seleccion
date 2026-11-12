package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class VistaReportes extends JFrame {

    private final Color NARANJA_PRINCIPAL = new Color(255, 107, 0);
    private final Color NARANJA_DEGRADADO = new Color(255, 145, 56);
    private final Color AZUL_OSCURO = new Color(15, 23, 42);
    private final Color FONDO_APP = new Color(244, 246, 249);
    private final Color TEXTO_OSCURO = new Color(30, 41, 59);

    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTable tblReportes;

    public VistaReportes() {
        setTitle("ERP Papelería | Reportes Analíticos de Selección");
        setSize(950, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        construirInterfaz();
    }

    private void construirInterfaz() {
        // 1. Header Naranja
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
        header.setPreferredSize(new Dimension(950, 58));
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        JLabel lblTitulo = new JLabel("SELECCIÓN  |  REPORTES Y ESTADÍSTICAS");
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

        // Tarjeta de Filtros en 2 filas ordenadas para que no se corte nada
        JPanel cardFiltros = crearTarjetaBlanca();
        cardFiltros.setLayout(new GridLayout(2, 1, 0, 12));

        // Fila 1: Fechas
        JPanel filaFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filaFechas.setOpaque(false);
        filaFechas.add(crearEtiqueta("Desde (AAAA-MM-DD):"));
        txtFechaInicio = crearCampoTexto("2026-01-01");
        filaFechas.add(txtFechaInicio);

        filaFechas.add(Box.createHorizontalStrut(15));
        filaFechas.add(crearEtiqueta("Hasta (AAAA-MM-DD):"));
        txtFechaFin = crearCampoTexto("2026-12-31");
        filaFechas.add(txtFechaFin);

        // Fila 2: Botones de Reporte bien visibles
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filaBotones.setOpaque(false);

        JButton btnRanking = new JButton("Generar Ranking de Evaluados");
        btnRanking.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRanking.setForeground(Color.WHITE);
        btnRanking.setBackground(NARANJA_PRINCIPAL);
        btnRanking.setFocusPainted(false);
        btnRanking.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRanking.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRanking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnRanking.setBackground(NARANJA_DEGRADADO); }
            @Override
            public void mouseExited(MouseEvent e) { btnRanking.setBackground(NARANJA_PRINCIPAL); }
        });
        btnRanking.addActionListener(e -> 
            ReporteServicio.cargarRanking(tblReportes, txtFechaInicio.getText().trim(), txtFechaFin.getText().trim())
        );

        JButton btnMatriz = new JButton("Generar Matriz Cruzada (PIVOT)");
        btnMatriz.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMatriz.setForeground(Color.WHITE);
        btnMatriz.setBackground(AZUL_OSCURO);
        btnMatriz.setFocusPainted(false);
        btnMatriz.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnMatriz.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMatriz.addActionListener(e -> 
            ReporteServicio.cargarMatriz(tblReportes, txtFechaInicio.getText().trim(), txtFechaFin.getText().trim())
        );

        filaBotones.add(btnRanking);
        filaBotones.add(btnMatriz);

        cardFiltros.add(filaFechas);
        cardFiltros.add(filaBotones);

        contenedor.add(cardFiltros, BorderLayout.NORTH);

        // 3. Tarjeta Resultados
        JPanel cardTabla = crearTarjetaBlanca();
        cardTabla.setLayout(new BorderLayout(0, 10));

        JLabel lblRes = new JLabel("Resultados de la Consulta Analítica");
        lblRes.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblRes.setForeground(TEXTO_OSCURO);
        cardTabla.add(lblRes, BorderLayout.NORTH);

        tblReportes = new JTable();
        tblReportes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblReportes.setRowHeight(30);
        tblReportes.setGridColor(new Color(241, 245, 249));
        tblReportes.setSelectionBackground(new Color(255, 237, 213));

        JTableHeader th = tblReportes.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(AZUL_OSCURO);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 34));

        JScrollPane scroll = new JScrollPane(tblReportes);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);
        cardTabla.add(scroll, BorderLayout.CENTER);

        contenedor.add(cardTabla, BorderLayout.CENTER);
        add(contenedor, BorderLayout.CENTER);
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

    private JTextField crearCampoTexto(String valorDefecto) {
        JTextField txt = new JTextField(valorDefecto);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(130, 34));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return txt;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaReportes().setVisible(true));
    }
}
