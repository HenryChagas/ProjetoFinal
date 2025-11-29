package com.gps.config;

/**
 *
 * @author Henry Wallace
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    // Configurações do seu banco de dados
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3307/ordensdeservico"; 
    private static final String USER = "root";                                         
    private static final String PASS = "";                             

    // Método principal para obter a conexão
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Driver do banco de dados não encontrado: ", ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Erro na conexão com o BD: ", ex);
        }
    }
    
    // Métodos de fechamento de recursos 

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar conexão: " + ex);
            }
        }
    }

    public static void closeConnection(Connection con, PreparedStatement stmt) {
        closeConnection(con);
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar Statement: " + ex);
            }
        }
    }

    public static void closeConnection(Connection con, PreparedStatement stmt, ResultSet rs) {
        closeConnection(con, stmt);
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar ResultSet: " + ex);
            }
        }
    }
}