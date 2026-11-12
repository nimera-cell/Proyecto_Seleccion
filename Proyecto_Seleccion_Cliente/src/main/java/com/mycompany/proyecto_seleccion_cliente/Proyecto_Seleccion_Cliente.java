package com.mycompany.proyecto_seleccion_cliente;

import javax.swing.SwingUtilities;

public class Proyecto_Seleccion_Cliente {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaLogin().setVisible(true);
        });
    }
}
