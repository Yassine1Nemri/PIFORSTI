package com.melocode.videoismaael.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bdforsti1";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection connection;

    public static Connection getConnection() {
        try {
            // Vérifier si la connexion est null ou fermée
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion établie avec succès !");
            }
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Ajouter une méthode pour fermer la connexion
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}