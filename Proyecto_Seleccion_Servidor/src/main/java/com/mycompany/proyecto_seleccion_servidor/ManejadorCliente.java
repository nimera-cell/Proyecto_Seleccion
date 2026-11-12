package com.mycompany.proyecto_seleccion_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManejadorCliente extends Thread {
    private Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("[SERVIDOR] Cliente conectado desde: " + socket.getRemoteSocketAddress());
            String peticion;

            while ((peticion = entrada.readLine()) != null) {
                System.out.println("[CLIENTE DICE]: " + peticion);

                if (peticion.startsWith("VALIDAR_LOGIN")) {
                    validarUsuario(peticion, salida);
                } else if (peticion.equals("LISTAR_CANDIDATOS")) {
                    enviarCandidatos(salida);
                } else if (peticion.startsWith("REGISTRAR_CANDIDATO")) {
                    registrarCandidato(peticion, salida);
                } else if (peticion.equals("LISTAR_PARAMETROS")) {
                    enviarParametros(salida);
                } else if (peticion.startsWith("REGISTRAR_PARAMETRO")) {
                    registrarParametro(peticion, salida);
                } else if (peticion.startsWith("GUARDAR_EVALUACION")) {
                    guardarEvaluacionCompleja(peticion, salida);
                } else if (peticion.startsWith("CONTRATAR_MEJORES")) {
                    ejecutarContratacion(peticion, salida);
                } else if (peticion.startsWith("REPORTE_RANKING")) {
                    enviarReporteRanking(peticion, salida);
                } else if (peticion.startsWith("REPORTE_MATRIZ")) {
                    enviarReporteMatriz(peticion, salida);
                } else {
                    salida.println("COMANDO_DESCONOCIDO");
                }
            }

        } catch (IOException e) {
            System.out.println("[Manejador] Cliente desconectado de forma abrupta.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("[ERROR] No se pudo cerrar el socket.");
            }
        }
    }

        private void validarUsuario(String peticion, PrintWriter salida) {
        String[] partes = peticion.split("\\|");
        if (partes.length < 3) {
            salida.println("LOGIN_ERROR|Datos incompletos");
            return;
        }
        String user = partes[1];
        String pass = partes[2];

        String sql = "SELECT id_usuario FROM usuarios WHERE username = ? AND password = ?";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    salida.println("LOGIN_OK|ADMINISTRADOR");
                } else {
                    salida.println("LOGIN_ERROR|Usuario o contraseña incorrectos");
                }
            }
        } catch (SQLException e) {
            salida.println("LOGIN_ERROR|Error en BD: " + e.getMessage());
        }
    }



    private void enviarCandidatos(PrintWriter salida) {
        String sql = "SELECT cedula, nombre, TO_CHAR(fecha_nacimiento, 'YYYY-MM-DD') AS fecha FROM candidatos";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                salida.println(rs.getString("cedula") + "|" + rs.getString("nombre") + "|" + rs.getString("fecha"));
            }
            salida.println("FIN_LISTA"); 
        } catch (SQLException e) {
            System.out.println("[SERVIDOR ERROR] Falló listar candidatos: " + e.getMessage());
            salida.println("FIN_LISTA");
        }
    }

    private void registrarCandidato(String peticion, PrintWriter salida) {
        String[] partes = peticion.split("\\|");
        if (partes.length < 4) {
            salida.println("REGISTRO_ERROR|Datos incompletos");
            return;
        }
        String sql = "INSERT INTO candidatos (cedula, nombre, fecha_nacimiento) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, partes[1]);
            ps.setString(2, partes[2]);
            ps.setString(3, partes[3]);
            ps.executeUpdate();
            salida.println("REGISTRO_OK");
        } catch (SQLException e) {
            salida.println("REGISTRO_ERROR|" + e.getMessage());
        }
    }

    private void enviarParametros(PrintWriter salida) {
        String sql = "SELECT codigo_parametro, nombre_parametro, puntaje_maximo FROM parametros_evaluacion";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                salida.println(rs.getInt("codigo_parametro") + "|" + rs.getString("nombre_parametro") + "|" + rs.getDouble("puntaje_maximo"));
            }
            salida.println("FIN_LISTA"); 
        } catch (SQLException e) {
            System.out.println("[SERVIDOR ERROR] Falló listar parámetros: " + e.getMessage());
            salida.println("FIN_LISTA");
        }
    }

    private void registrarParametro(String peticion, PrintWriter salida) {
        String[] partes = peticion.split("\\|");
        if (partes.length < 3) {
            salida.println("REGISTRO_ERROR|Datos incompletos");
            return;
        }
        String sql = "INSERT INTO parametros_evaluacion (nombre_parametro, puntaje_maximo) VALUES (?, ?)";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, partes[1]);
            ps.setDouble(2, Double.parseDouble(partes[2]));
            ps.executeUpdate();
            salida.println("REGISTRO_OK");
        } catch (SQLException e) {
            salida.println("REGISTRO_ERROR|" + e.getMessage());
        }
    }

    private void guardarEvaluacionCompleja(String peticion, PrintWriter salida) {
        String[] partes = peticion.split("\\|");
        if (partes.length < 3) {
            salida.println("PROCESO_ERROR|Datos incompletos");
            return;
        }
        String cedula = partes[1];
        String detallesRaw = partes[2];
        Connection cn = null;
        try {
            cn = ConexionDB.getConexion();
            cn.setAutoCommit(false);

            String sqlCabecera = "INSERT INTO cabeceras_evaluacion (cedula_candidato) VALUES (?)";
            String[] colGeneradas = {"NUMERO_EVALUACION"};
            int idCabeceraGenerated = -1;
            try (PreparedStatement psCab = cn.prepareStatement(sqlCabecera, colGeneradas)) {
                psCab.setString(1, cedula);
                psCab.executeUpdate();
                try (ResultSet rsGen = psCab.getGeneratedKeys()) {
                    if (rsGen.next()) { idCabeceraGenerated = rsGen.getInt(1); }
                }
            }

            if (idCabeceraGenerated == -1) throw new SQLException("ID no generado");

            String sqlDetalle = "INSERT INTO detalles_evaluacion (numero_evaluacion, codigo_parametro, calificacion) VALUES (?, ?, ?)";
            try (PreparedStatement psDet = cn.prepareStatement(sqlDetalle)) {
                String[] registrosDetalle = detallesRaw.split(";");
                for (String reg : registrosDetalle) {
                    String[] datosIdNota = reg.split(",");
                    psDet.setInt(1, idCabeceraGenerated);
                    psDet.setInt(2, Integer.parseInt(datosIdNota[0]));
                    psDet.setDouble(3, Double.parseDouble(datosIdNota[1]));
                    psDet.executeUpdate();
                }
            }
            cn.commit();
            salida.println("PROCESO_OK");
        } catch (SQLException e) {
            if (cn != null) { try { cn.rollback(); } catch (SQLException ex) {} }
            salida.println("PROCESO_ERROR|" + e.getMessage());
        } finally {
            if (cn != null) { try { cn.close(); } catch (SQLException e) {} }
        }
    }

          private void ejecutarContratacion(String peticion, PrintWriter salida) {
        String[] partes = peticion.split("\\|");
        int cantidad = Integer.parseInt(partes[1]);
        double sueldo = Double.parseDouble(partes[2]);
        String sqlCall = "{call sp_contratar_mejores(?, ?)}";
        try (Connection cn = ConexionDB.getConexion();
             CallableStatement cs = cn.prepareCall(sqlCall)) {
            cs.setInt(1, cantidad);
            cs.setDouble(2, sueldo);
            cs.execute();
            salida.println("CONTRATAR_OK");
        } catch (SQLException e) {
            salida.println("CONTRATAR_ERROR|" + e.getMessage());
        }
    }

    private void enviarReporteRanking(String peticion, PrintWriter salida) {
        String[] partes = peticion.split("\\|");
        if (partes.length < 3) {
            salida.println("FIN_LISTA");
            return;
        }
        String fechaIni = partes[1];
        String fechaFin = partes[2];

        String sql = "SELECT c.nombre, SUM(de.calificacion) AS total " +
                     "FROM candidatos c " +
                     "JOIN cabeceras_evaluacion ce ON c.cedula = ce.cedula_candidato " +
                     "JOIN detalles_evaluacion de ON ce.numero_evaluacion = de.numero_evaluacion " +
                     "WHERE TO_CHAR(ce.fecha, 'YYYY-MM-DD') BETWEEN ? AND ? " +
                     "GROUP BY c.cedula, c.nombre " +
                     "ORDER BY total DESC";

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, fechaIni);
            ps.setString(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    salida.println(rs.getString("nombre") + "|" + rs.getDouble("total"));
                }
            }
            salida.println("FIN_LISTA");
        } catch (SQLException e) {
            System.out.println("[SERVIDOR ERROR] Falló Ranking: " + e.getMessage());
            salida.println("FIN_LISTA");
        }
    }

    private void enviarReporteMatriz(String peticion, PrintWriter salida) {
        String[] partes = peticion.split("\\|");
        if (partes.length < 3) {
            salida.println("FIN_LISTA");
            return;
        }
        String fechaIni = partes[1];
        String fechaFin = partes[2];

        String sql = "SELECT c.nombre AS candidato, " +
                     "       NVL(SUM(CASE WHEN de.codigo_parametro = 1 THEN de.calificacion END), 0) AS tecnica, " +
                     "       NVL(SUM(CASE WHEN de.codigo_parametro = 2 THEN de.calificacion END), 0) AS psicologica, " +
                     "       NVL(SUM(CASE WHEN de.codigo_parametro = 3 THEN de.calificacion END), 0) AS experiencia " +
                     "FROM candidatos c " +
                     "JOIN cabeceras_evaluacion ce ON c.cedula = ce.cedula_candidato " +
                     "JOIN detalles_evaluacion de ON ce.numero_evaluacion = de.numero_evaluacion " +
                     "WHERE TO_CHAR(ce.fecha, 'YYYY-MM-DD') BETWEEN ? AND ? " +
                     "GROUP BY c.cedula, c.nombre " +
                     "ORDER BY c.nombre";

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, fechaIni);
            ps.setString(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String fila = rs.getString("candidato") + "|" +
                                  rs.getDouble("tecnica") + "|" +
                                  rs.getDouble("psicologica") + "|" +
                                  rs.getDouble("experiencia");
                    salida.println(fila);
                }
            }
            salida.println("FIN_LISTA");
        } catch (SQLException e) {
            System.out.println("[SERVIDOR ERROR] Falló Matriz: " + e.getMessage());
            salida.println("FIN_LISTA");
        }
    }
}

