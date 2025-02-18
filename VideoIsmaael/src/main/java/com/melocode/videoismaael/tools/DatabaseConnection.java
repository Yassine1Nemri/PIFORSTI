package com.melocode.videoismaael.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bdforsti1";  // Nom de ta base de données
    private static final String USER = "root";  // Ton utilisateur MySQL
    private static final String PASSWORD = "";  // Ton mot de passe MySQL si nécessaire

    public static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Établir la connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion réussie à la base de données !");
            } catch (SQLException e) {
                System.out.println("❌ Erreur de connexion à la base de données !");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
