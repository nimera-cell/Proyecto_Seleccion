package com.mycompany.proyecto_seleccion_servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Proyecto_Seleccion_Servidor {
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("[SERVIDOR] Escuchando peticiones concurrentes en el puerto " + PUERTO + "...");

            while (true) {
                // Acepta la conexión de un cliente (ej. la interfaz gráfica)
                Socket clienteSocket = serverSocket.accept();
                
                // Crea un nuevo hilo concurrente para atenderlo sin congelar el servidor
                ManejadorCliente hilo = new ManejadorCliente(clienteSocket);
                hilo.start();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Error en el servidor de sockets: " + e.getMessage());
        }
    }
}
