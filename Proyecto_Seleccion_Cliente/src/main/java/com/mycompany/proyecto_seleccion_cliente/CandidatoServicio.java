package com.mycompany.proyecto_seleccion_cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class CandidatoServicio {

    // 1. FUNCIÓN PARA LISTAR: Solicita los datos al servidor y llena la JTable
    public static void cargarTablaCandidatos(JTable tabla) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre Completo");
        modelo.addColumn("Fecha Nacimiento");
        
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            salida.println("LISTAR_CANDIDATOS");
            
            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                if (respuesta.equals("FIN_LISTA")) {
                    break;
                }
                String[] datos = respuesta.split("\\|");
                modelo.addRow(datos);
            }
            tabla.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de red al listar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 2. FUNCIÓN PARA GUARDAR (El paso 2 por el que preguntabas): Envía datos al servidor
    public static boolean insertarCandidato(String cedula, String nombre, String fecha) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            // Empaquetamos la cadena con el protocolo de bajo nivel
            salida.println("REGISTRAR_CANDIDATO|" + cedula + "|" + nombre + "|" + fecha);
            
            String respuesta = entrada.readLine();
            if (respuesta != null && respuesta.equals("REGISTRO_OK")) {
                return true;
            } else if (respuesta != null && respuesta.startsWith("REGISTRO_ERROR")) {
                String[] partes = respuesta.split("\\|");
                JOptionPane.showMessageDialog(null, "Error en Oracle: " + partes[1], "Error en Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de red al guardar: " + e.getMessage(), "Error de Red", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
