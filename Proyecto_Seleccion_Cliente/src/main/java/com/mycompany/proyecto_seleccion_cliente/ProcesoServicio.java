package com.mycompany.proyecto_seleccion_cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class ProcesoServicio {

    // 1. Llena el ComboBox de Candidatos con formato "Cédula - Nombre"
    public static void cargarComboCandidatos(JComboBox<String> combo) {
        combo.removeAllItems();
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            salida.println("LISTAR_CANDIDATOS");
            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                if (respuesta.equals("FIN_LISTA")) break;
                String[] datos = respuesta.split("\\|");
                combo.addItem(datos[0] + " - " + datos[1]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar candidatos: " + e.getMessage());
        }
    }

    // 2. Llena el ComboBox de Parámetros con formato "Código - Nombre (Max: Puntaje)"
    public static void cargarComboParametros(JComboBox<String> combo) {
        combo.removeAllItems();
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            salida.println("LISTAR_PARAMETROS");
            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                if (respuesta.equals("FIN_LISTA")) break;
                String[] datos = respuesta.split("\\|");
                combo.addItem(datos[0] + " - " + datos[1] + " (Max: " + datos[2] + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar parámetros: " + e.getMessage());
        }
    }

    // 3. Envía la transacción Cabecera-Detalle empaquetada al servidor
    public static boolean registrarEvaluacion(String cedula, String detallesEstructurados) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            salida.println("GUARDAR_EVALUACION|" + cedula + "|" + detallesEstructurados);
            String respuesta = entrada.readLine();
            
            if (respuesta != null && respuesta.equals("PROCESO_OK")) {
                return true;
            } else if (respuesta != null && respuesta.startsWith("PROCESO_ERROR")) {
                String[] partes = respuesta.split("\\|");
                JOptionPane.showMessageDialog(null, partes[1], "Error de Validación en Oracle", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de red al guardar evaluación: " + e.getMessage());
        }
        return false;
    }

    // 4. Llama al procedimiento almacenado de Oracle para contratar
    public static boolean ejecutarContratacion(int cantidad, double sueldo) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            salida.println("CONTRATAR_MEJORES|" + cantidad + "|" + sueldo);
            String respuesta = entrada.readLine();
            
            if (respuesta != null && respuesta.equals("CONTRATAR_OK")) {
                return true;
            } else if (respuesta != null && respuesta.startsWith("CONTRATAR_ERROR")) {
                String[] partes = respuesta.split("\\|");
                JOptionPane.showMessageDialog(null, partes[1], "Error en Procedimiento", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de red al contratar: " + e.getMessage());
        }
        return false;
    }
}
