package com.mycompany.proyecto_seleccion_cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReporteServicio {

    // 1. Llena la tabla con el Ranking de Evaluados
    public static void cargarRanking(JTable tabla, String fechaIni, String fechaFin) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre del Evaluado");
        modelo.addColumn("Puntaje Total");

        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            salida.println("REPORTE_RANKING|" + fechaIni + "|" + fechaFin);
            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                if (respuesta.equals("FIN_LISTA")) break;
                modelo.addRow(respuesta.split("\\|"));
            }
            tabla.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar Ranking: " + e.getMessage());
        }
    }

    // 2. Llena la tabla con la Matriz Cruzada (PIVOT)
    public static void cargarMatriz(JTable tabla, String fechaIni, String fechaFin) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Candidato");
        modelo.addColumn("Prueba Técnica");
        modelo.addColumn("Psicológica");
        modelo.addColumn("Experiencia");

        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            salida.println("REPORTE_MATRIZ|" + fechaIni + "|" + fechaFin);
            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                if (respuesta.equals("FIN_LISTA")) break;
                modelo.addRow(respuesta.split("\\|"));
            }
            tabla.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar Matriz: " + e.getMessage());
        }
    }
}
