package com.mycompany.proyecto_seleccion_servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USER = "system";
    private static final String PASSWORD = "isaac123"; // <-- Pon tu contraseña real aquí

    public static Connection getConexion() {
        Connection cn = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            cn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[BASE DE DATOS] Conectado exitosamente a Oracle.");
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] No se encontró el driver JDBC de Oracle.");
        } catch (SQLException e) {
            System.out.println("[ERROR] Error de conexión a Oracle: " + e.getMessage());
        }
        return cn;
    }
}
