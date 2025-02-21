package com.melocode.videoismaael.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.melocode.videoismaael.tools.DatabaseConnection.connection;

public class Reclamation {
    private int id_rec;
    private int id_user;
    private String sujet;
    private String description;
    private String status;
    private LocalDateTime date_creation;

    // Default constructor
    public Reclamation() {
        this.date_creation = LocalDateTime.now();
        this.status = "pending";
    }

    // Constructor without ID
    public Reclamation(int id_user, String sujet, String description) {
        this.id_user = id_user;
        this.sujet = sujet;
        this.description = description;
        this.status = "pending";
        this.date_creation = LocalDateTime.now();
    }

    // Full constructor
    public Reclamation(int id_rec, int id_user, String sujet, String description, String status, LocalDateTime date_creation) {
        this.id_rec = id_rec;
        this.id_user = id_user;
        this.sujet = sujet;
        this.description = description;
        this.status = status;
        this.date_creation = date_creation;
    }

    public Reclamation(String sujet, String status, String dateCreation) {
    }

    // Getters and Setters
    public int getId_rec() {
        return id_rec;
    }

    public void setId_rec(int id_rec) {
        this.id_rec = id_rec;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(LocalDateTime date_creation) {
        this.date_creation = date_creation;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id_rec=" + id_rec +
                ", id_user=" + id_user +
                ", sujet='" + sujet + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", date_creation=" + date_creation +
                '}';
    }

    public Reclamation getReclamationById(int reclamationId) {
        Reclamation reclamation = null;
        try {
            // SQL query to select a reclamation by ID
            String query = "SELECT * FROM reclamation WHERE id = ?";

            // Get database connection and prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reclamationId);

            // Execute query and get result
            ResultSet resultSet = preparedStatement.executeQuery();

            // If result exists, create new Reclamation object
            if (resultSet.next()) {
                reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setTitle(resultSet.getString("title"));
                reclamation.setDescription(resultSet.getString("description"));
                reclamation.setStatus(resultSet.getString("status"));
                // Add other fields as needed based on your Reclamation entity
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving reclamation: " + e.getMessage());
        }

        return reclamation;
    }

    private void setId(int id) {
    }

    private void setTitle(String title) {
    }

    public Object getId() {
        return null;
    }
}
