package com.mycompany.proyecto_seleccion_cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class ParametroServicio {

    // 1. MÉTODO PARA LISTAR: Carga las columnas Código, Criterio y Puntaje Máximo
    public static void cargarTablaParametros(JTable tabla) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Criterio de Evaluación");
        modelo.addColumn("Puntaje Máximo");
        
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            salida.println("LISTAR_PARAMETROS");
            
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
            JOptionPane.showMessageDialog(null, "Error al conectar para listar parámetros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 2. MÉTODO PARA GUARDAR: Envía el nombre y puntaje máximo del parámetro a Oracle
    public static boolean insertarParametro(String nombre, String puntaje) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            salida.println("REGISTRAR_PARAMETRO|" + nombre + "|" + puntaje);
            
            String respuesta = entrada.readLine();
            if (respuesta != null && respuesta.equals("REGISTRO_OK")) {
                return true;
            } else if (respuesta != null && respuesta.startsWith("REGISTRO_ERROR")) {
                String[] partes = respuesta.split("\\|");
                JOptionPane.showMessageDialog(null, "Error en Oracle: " + partes[1], "Error en Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de red al guardar parámetro: " + e.getMessage(), "Error de Red", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
