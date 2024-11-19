package com.vitor.java.gs2_spring.connection;

import java.sql.*;

public class Connect {

    private static final String url = "jdbc:mysql://localhost:3306/sunny_meter";
    private static final String user = "root";
    private static final String password = System.getenv("DB_PASSWORD");

    public static Connection connect() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected!");
            } else {
                System.out.println("Failed to connect.");
            }
        } catch (SQLException e) {
            System.out.println("Error: couldn't connect to database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado" + e.getMessage());
        }
        return conn;
    }
}
