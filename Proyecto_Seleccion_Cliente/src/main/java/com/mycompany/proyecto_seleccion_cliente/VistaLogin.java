package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class VistaLogin extends JFrame {

    private final Color NARANJA_PRINCIPAL = new Color(255, 107, 0);
    private final Color NARANJA_DEGRADADO = new Color(255, 145, 56);
    private final Color AZUL_OSCURO = new Color(15, 23, 42);
    private final Color FONDO_APP = new Color(244, 246, 249);
    private final Color TEXTO_OSCURO = new Color(30, 41, 59);
    private final Color TEXTO_SECUNDARIO = new Color(100, 116, 139);

    private JTextField txtUsuario;
    private JPasswordField txtClave;

    public VistaLogin() {
        setTitle("ERP Papelería | Inicio de Sesión");
        setSize(440, 520);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        construirInterfaz();
    }

    private void construirInterfaz() {
        // 1. Cabecera con Degradado Naranja
        JPanel header = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, NARANJA_PRINCIPAL, getWidth(), getHeight(), NARANJA_DEGRADADO));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(440, 145));

        JPanel panelTitulos = new JPanel(new GridLayout(2, 1, 0, 5));
        panelTitulos.setOpaque(false);

        JLabel lblSistema = new JLabel("PAPELERÍA ERP", SwingConstants.CENTER);
        lblSistema.setForeground(Color.WHITE);
        lblSistema.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel lblModulo = new JLabel("Módulo de Selección de Personal", SwingConstants.CENTER);
        lblModulo.setForeground(new Color(255, 243, 232));
        lblModulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panelTitulos.add(lblSistema);
        panelTitulos.add(lblModulo);
        header.add(panelTitulos);

        add(header, BorderLayout.NORTH);

        // 2. Formulario Central
        JPanel cuerpo = new JPanel(new GridBagLayout());
        cuerpo.setBackground(FONDO_APP);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(25, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel lblBienvenida = new JLabel("Bienvenido de nuevo");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(TEXTO_OSCURO);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        cuerpo.add(lblBienvenida, gbc);

        JLabel lblInstruccion = new JLabel("Ingresa tus credenciales para continuar");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInstruccion.setForeground(TEXTO_SECUNDARIO);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 22, 0);
        cuerpo.add(lblInstruccion, gbc);

        // Campo Usuario
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(TEXTO_OSCURO);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        cuerpo.add(lblUser, gbc);

        txtUsuario = new JTextField();
        estilizarCampo(txtUsuario);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 16, 0);
        cuerpo.add(txtUsuario, gbc);

        // Campo Contraseña
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(TEXTO_OSCURO);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 6, 0);
        cuerpo.add(lblPass, gbc);

        txtClave = new JPasswordField();
        estilizarCampo(txtClave);
        // Permitir iniciar sesión presionando Enter
        txtClave.addActionListener(e -> iniciarSesion());
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 26, 0);
        cuerpo.add(txtClave, gbc);

        // Botón Ingresar
        JButton btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(NARANJA_PRINCIPAL);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(0, 42));
        btnIngresar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnIngresar.setBackground(NARANJA_DEGRADADO); }
            @Override
            public void mouseExited(MouseEvent e) { btnIngresar.setBackground(NARANJA_PRINCIPAL); }
        });
        btnIngresar.addActionListener(e -> iniciarSesion());

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        cuerpo.add(btnIngresar, gbc);

        add(cuerpo, BorderLayout.CENTER);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(0, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Conectar por socket al servidor para validar en Oracle
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            salida.println("VALIDAR_LOGIN|" + usuario + "|" + clave);
            String respuesta = entrada.readLine();

            if (respuesta != null && respuesta.startsWith("LOGIN_OK")) {
                // Cerrar el Login y abrir el Menú Principal
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    new FrmMenuSeleccion().setVisible(true);
                });
            } else if (respuesta != null && respuesta.startsWith("LOGIN_ERROR")) {
                String[] partes = respuesta.split("\\|");
                JOptionPane.showMessageDialog(this, partes[1], "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar con el servidor (Puerto 5000).", "Error de Red", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaLogin().setVisible(true));
    }
}
