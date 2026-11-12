package com.mycompany.proyecto_seleccion_cliente;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class FrmMenuSeleccion extends JFrame {

    // Paleta Corporativa Moderna
    private final Color NARANJA_PRINCIPAL = new Color(255, 107, 0);
    private final Color NARANJA_DEGRADADO = new Color(255, 145, 56);
    private final Color NARANJA_SUAVE_BG = new Color(255, 243, 232);
    private final Color AZUL_SIDEBAR = new Color(15, 23, 42);
    private final Color AZUL_HOVER = new Color(30, 41, 59);
    private final Color FONDO_APP = new Color(244, 246, 249);
    private final Color TEXTO_OSCURO = new Color(30, 41, 59);
    private final Color TEXTO_SECUNDARIO = new Color(100, 116, 139);

    public FrmMenuSeleccion() {
        configurarVentana();
        construirInterfaz();
    }

    private void configurarVentana() {
        setTitle("ERP Papelería | Módulo de Selección");
        setSize(1020, 620);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void construirInterfaz() {
        // =====================================================================
        // 1. HEADER SUPERIOR CON DEGRADADO NARANJA
        // =====================================================================
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, NARANJA_PRINCIPAL, getWidth(), 0, NARANJA_DEGRADADO);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(1020, 65));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Logo vectorial + Título
        JPanel panelMarca = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 18));
        panelMarca.setOpaque(false);
        panelMarca.add(new IconoVectorial("LUPA", Color.WHITE, 22));
        
        JLabel lblMarca = new JLabel("PAPELERÍA ERP  |  MÓDULO DE SELECCIÓN");
        lblMarca.setForeground(Color.WHITE);
        lblMarca.setFont(new Font("Segoe UI", Font.BOLD, 17));
        panelMarca.add(lblMarca);

        // Píldora limpia de usuario (sin cuadros raros)
        JPanel badgeUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        badgeUsuario.setOpaque(false);
        badgeUsuario.add(new IconoVectorial("USER", Color.WHITE, 16));
        
        JLabel lblUser = new JLabel("admin  |  Puerto 5000");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badgeUsuario.add(lblUser);

        JPanel contenedorDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 15));
        contenedorDerecho.setOpaque(false);
        contenedorDerecho.add(badgeUsuario);

        header.add(panelMarca, BorderLayout.WEST);
        header.add(contenedorDerecho, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // =====================================================================
        // 2. SIDEBAR LATERAL MODERNO CON ÍCONOS VECTORIALES
        // =====================================================================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(AZUL_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(235, 555));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 18, 25, 18));

        JLabel lblMenuTitle = new JLabel("NAVEGACIÓN");
        lblMenuTitle.setForeground(new Color(148, 163, 184));
        lblMenuTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenuTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 12, 0));
        sidebar.add(lblMenuTitle);

        sidebar.add(crearBotonSidebar("Candidatos", "CANDIDATOS", () -> abrirVentana(new VistaCandidatos())));
sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
sidebar.add(crearBotonSidebar("Parámetros", "EVALUACIONES", () -> abrirVentana(new VistaParametros())));
sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
sidebar.add(crearBotonSidebar("Procesos", "PROCESOS", () -> abrirVentana(new VistaProceso())));
sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
sidebar.add(crearBotonSidebar("Reportes", "REPORTES", () -> abrirVentana(new VistaReportes())));


        sidebar.add(Box.createVerticalGlue());

        sidebar.add(crearBotonSidebar("Menú Principal", "VOLVER", () -> {
            JOptionPane.showMessageDialog(this, "Conectando con el menú principal integrado...");
        }));

        add(sidebar, BorderLayout.WEST);

        // =====================================================================
        // 3. ÁREA DE CONTENIDO PRINCIPAL
        // =====================================================================
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(FONDO_APP);
        contenido.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));

        // Encabezado de sección con ícono vectorial grande
        JPanel cabeceraSeccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        cabeceraSeccion.setBackground(FONDO_APP);
        cabeceraSeccion.add(new IconoVectorial("CANDIDATOS", NARANJA_PRINCIPAL, 38));

        JPanel textosCabecera = new JPanel(new GridLayout(2, 1, 0, 2));
        textosCabecera.setBackground(FONDO_APP);

        JLabel lblBienvenida = new JLabel("Panel de Control - Selección de Personal");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBienvenida.setForeground(TEXTO_OSCURO);

        JLabel lblDescripcion = new JLabel("Administra candidatos, define rúbricas de evaluación, califica postulantes y ejecuta contrataciones.");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);

        textosCabecera.add(lblBienvenida);
        textosCabecera.add(lblDescripcion);
        cabeceraSeccion.add(textosCabecera);

        contenido.add(cabeceraSeccion, BorderLayout.NORTH);

        // Grid 2x2 de Tarjetas Interactivas
        JPanel gridCards = new JPanel(new GridLayout(2, 2, 28, 28));
        gridCards.setBackground(FONDO_APP);
        gridCards.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

gridCards.add(new TarjetaModerna(
    "CANDIDATOS",
    "Candidatos",
    "Registra, actualiza y consulta la base de datos de postulantes.",
    () -> abrirVentana(new VistaCandidatos())
));

gridCards.add(new TarjetaModerna(
    "EVALUACIONES",
    "Parámetros de Evaluación",
    "Configura los criterios de calificación y sus puntajes máximos.",
    () -> abrirVentana(new VistaParametros())
));

gridCards.add(new TarjetaModerna(
    "PROCESOS",
    "Procesos y Contratación",
    "Asigna notas por candidato y promueve a los mejores a Nómina.",
    () -> abrirVentana(new VistaProceso())
));

gridCards.add(new TarjetaModerna(
    "REPORTES",
    "Reportes Analíticos",
    "Genera el ranking general y la matriz cruzada por rango de fechas.",
    () -> abrirVentana(new VistaReportes())
));


        contenido.add(gridCards, BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);
    }

    // Crea botones laterales elegantes con ícono vectorial y efecto hover
    private JPanel crearBotonSidebar(String titulo, String tipoIcono, Runnable accion) {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 11)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        btnPanel.setOpaque(false);
        btnPanel.setBackground(AZUL_SIDEBAR);
        btnPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPanel.setMaximumSize(new Dimension(200, 44));
        btnPanel.setPreferredSize(new Dimension(200, 44));

        IconoVectorial icono = new IconoVectorial(tipoIcono, Color.WHITE, 18);
        JLabel lblTexto = new JLabel(titulo);
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setFont(new Font("Segoe UI", Font.BOLD, 14));
btnPanel.add(icono);
btnPanel.add(lblTexto);
btnPanel.addMouseListener(new MouseAdapter() {
@Override
public void mouseEntered(MouseEvent e) {
btnPanel.setBackground(AZUL_HOVER);
lblTexto.setForeground(NARANJA_DEGRADADO);
icono.setColor(NARANJA_DEGRADADO);
btnPanel.repaint();
}
@Override
public void mouseExited(MouseEvent e) {
btnPanel.setBackground(AZUL_SIDEBAR);
lblTexto.setForeground(Color.WHITE);
icono.setColor(Color.WHITE);
btnPanel.repaint();
}
@Override
public void mouseClicked(MouseEvent e) {
accion.run();
}
});
return btnPanel;
}
private void abrirVentana(JFrame ventana) {
ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
ventana.setLocationRelativeTo(null);
ventana.setVisible(true);
}
// =========================================================================
// COMPONENTE VECTORIAL MINIMALISTA (Dibuja íconos reales en Graphics2D)
// =========================================================================
private class IconoVectorial extends JPanel {
private final String tipo;
private Color color;
private final int tamano;
public IconoVectorial(String tipo, Color color, int tamano) {
this.tipo = tipo;
this.color = color;
this.tamano = tamano;
setOpaque(false);
setPreferredSize(new Dimension(tamano, tamano));
}
public void setColor(Color c) {
this.color = c;
repaint();
}
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
Graphics2D g2 = (Graphics2D) g;
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2.setColor(color);
g2.setStroke(new BasicStroke(Math.max(1.8f, tamano / 12f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
int w = tamano;
int h = tamano;
switch (tipo) {
case "CANDIDATOS":
// Silueta de usuario principal + secundario minimalista
g2.drawOval(w / 4, h / 8, w / 3, h / 3);
g2.drawArc(w / 8, h / 2, (int) (w * 0.58), h / 2, 0, 180);
g2.drawOval((int) (w * 0.62), h / 5, w / 4, h / 4);
g2.drawArc((int) (w * 0.58), (int) (h * 0.55), w / 3, h / 3, 0, 160);
break;
case "EVALUACIONES":
// Portapapeles con líneas de lista
g2.drawRoundRect(w / 5, h / 6, (int) (w * 0.6), (int) (h * 0.72), 4, 4);
g2.drawRect(w / 3, h / 10, w / 3, h / 8);
g2.drawLine(w / 3, (int) (h * 0.42), (int) (w * 0.66), (int) (h * 0.42));
g2.drawLine(w / 3, (int) (h * 0.58), (int) (w * 0.66), (int) (h * 0.58));
g2.drawLine(w / 3, (int) (h * 0.74), (int) (w * 0.55), (int) (h * 0.74));
break;
case "PROCESOS":
// Engranaje / rueda de proceso minimalista
int cx = w / 2, cy = h / 2, r = w / 4;
g2.drawOval(cx - r, cy - r, r * 2, r * 2);
for (int i = 0; i < 8; i++) {
double ang = Math.toRadians(i * 45);
int x1 = (int) (cx + Math.cos(ang) * (r + 1));
int y1 = (int) (cy + Math.sin(ang) * (r + 1));
int x2 = (int) (cx + Math.cos(ang) * (r + w / 6));
int y2 = (int) (cy + Math.sin(ang) * (r + h / 6));
g2.drawLine(x1, y1, x2, y2);
}
break;
case "REPORTES":
// Gráfico de barras ascendente
g2.fillRoundRect(w / 6, (int) (h * 0.55), w / 6, (int) (h * 0.32), 3, 3);
g2.fillRoundRect((int) (w * 0.42), (int) (h * 0.35), w / 6, (int) (h * 0.52), 3, 3);
g2.fillRoundRect((int) (w * 0.68), (int) (h * 0.15), w / 6, (int) (h * 0.72), 3, 3);
break;
case "LUPA":
g2.drawOval(w / 6, h / 6, (int) (w * 0.5), (int) (h * 0.5));
g2.drawLine((int) (w * 0.6), (int) (h * 0.6), (int) (w * 0.85), (int) (h * 0.85));
break;
case "USER":
g2.drawOval(w / 3, h / 8, w / 3, h / 3);
g2.drawArc(w / 6, (int) (h * 0.52), (int) (w * 0.66), h / 2, 0, 180);
break;
case "VOLVER":
g2.drawLine(w / 5, h / 2, (int) (w * 0.8), h / 2);
g2.drawLine(w / 5, h / 2, (int) (w * 0.45), h / 4);
g2.drawLine(w / 5, h / 2, (int) (w * 0.45), (int) (h * 0.75));
break;
case "FLECHA_DER":
g2.drawLine(w / 4, h / 2, (int) (w * 0.75), h / 2);
g2.drawLine((int) (w * 0.52), h / 4, (int) (w * 0.75), h / 2);
g2.drawLine((int) (w * 0.52), (int) (h * 0.75), (int) (w * 0.75), h / 2);
break;
}
}
}
// =========================================================================
// TARJETA MODERNA CON ÍCONOS VECTORIALES Y EFECTO HOVER
// =========================================================================
private class TarjetaModerna extends JPanel {
private boolean hover = false;
public TarjetaModerna(String tipoIcono, String titulo, String descripcion, Runnable accion) {
setLayout(new BorderLayout(15, 12));
setOpaque(false);
setCursor(new Cursor(Cursor.HAND_CURSOR));
setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));
// Contenedor redondeado para el ícono vectorial
JPanel badgeIcono = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 11)) {
@Override
protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g;
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2.setColor(hover ? NARANJA_PRINCIPAL : NARANJA_SUAVE_BG);
g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
super.paintComponent(g);
}
};
badgeIcono.setOpaque(false);
badgeIcono.setPreferredSize(new Dimension(50, 50));
IconoVectorial iconoCentral = new IconoVectorial(tipoIcono, NARANJA_PRINCIPAL, 26);
badgeIcono.add(iconoCentral);
JPanel topRow = new JPanel(new BorderLayout());
topRow.setOpaque(false);
topRow.add(badgeIcono, BorderLayout.WEST);
IconoVectorial flechaDerecha = new IconoVectorial("FLECHA_DER", new Color(203, 213, 225), 20);
JPanel panelFlecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 14));
panelFlecha.setOpaque(false);
panelFlecha.add(flechaDerecha);
topRow.add(panelFlecha, BorderLayout.EAST);
// Textos de la tarjeta
JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 6));
textPanel.setOpaque(false);
JLabel lblTitulo = new JLabel(titulo);
lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
lblTitulo.setForeground(TEXTO_OSCURO);
JLabel lblDesc = new JLabel("" + descripcion + "");
lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
lblDesc.setForeground(TEXTO_SECUNDARIO);
textPanel.add(lblTitulo);
textPanel.add(lblDesc);
add(topRow, BorderLayout.NORTH);
add(textPanel, BorderLayout.CENTER);
// Interactividad
addMouseListener(new MouseAdapter() {
@Override
public void mouseEntered(MouseEvent e) {
hover = true;
iconoCentral.setColor(Color.WHITE);
flechaDerecha.setColor(NARANJA_PRINCIPAL);
repaint();
}
@Override
public void mouseExited(MouseEvent e) {
hover = false;
iconoCentral.setColor(NARANJA_PRINCIPAL);
flechaDerecha.setColor(new Color(203, 213, 225));
repaint();
}
@Override
public void mouseClicked(MouseEvent e) {
accion.run();
}
});
}
@Override
protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g;
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
// Sombra suave
g2.setColor(new Color(0, 0, 0, hover ? 18 : 8));
g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 5, 20, 20);
// Fondo blanco
g2.setColor(Color.WHITE);
g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);
// Borde dinámico naranja
g2.setStroke(new BasicStroke(hover ? 2.0f : 1.0f));
g2.setColor(hover ? NARANJA_PRINCIPAL : new Color(226, 232, 240));
g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 20, 20);
}
}
public static void main(String[] args) {
java.awt.EventQueue.invokeLater(() -> {
new FrmMenuSeleccion().setVisible(true);
});
}
}