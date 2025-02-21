package com.melocode.videoismaael.services;

import com.melocode.videoismaael.entities.Reclamation;
import com.melocode.videoismaael.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements ICrud<Reclamation> {
    private Connection cnx;

    public ReclamationService() {
        this.cnx = DatabaseConnection.getConnection();
    }

    @Override
    public void ajouterEntite(Reclamation reclamation) {
        String query = "INSERT INTO reclamation (id_user, sujet, description, status, date_creation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setInt(1, reclamation.getId_user());
            st.setString(2, reclamation.getSujet());
            st.setString(3, reclamation.getDescription());
            st.setString(4, reclamation.getStatus());
            st.setTimestamp(5, Timestamp.valueOf(reclamation.getDate_creation()));

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reclamation ajoutée avec succès!");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réclamation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Reclamation> afficherEntite() {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Reclamation reclamation = new Reclamation(
                        rs.getInt("id_rec"),
                        rs.getInt("id_user"),
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getTimestamp("date_creation").toLocalDateTime()
                );
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations: " + e.getMessage());
            e.printStackTrace();
        }
        return reclamations;
    }

    @Override
    public void modifierEntite(Reclamation reclamation) {
        String query = "UPDATE reclamation SET sujet=?, description=?, status=? WHERE id_rec=?";
        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setString(1, reclamation.getSujet());
            st.setString(2, reclamation.getDescription());
            st.setString(3, reclamation.getStatus());
            st.setInt(4, reclamation.getId_rec());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reclamation modifiée avec succès!");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la réclamation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerEntite(Reclamation reclamation) {
        String query = "DELETE FROM reclamation WHERE id_rec=?";
        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setInt(1, reclamation.getId_rec());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reclamation supprimée avec succès!");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réclamation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Additional useful methods
    public List<Reclamation> getReclamationsByUser(int userId) {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE id_user = ?";

        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Reclamation reclamation = new Reclamation(
                        rs.getInt("id_rec"),
                        rs.getInt("id_user"),
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getTimestamp("date_creation").toLocalDateTime()
                );
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations: " + e.getMessage());
            e.printStackTrace();
        }
        return reclamations;
    }

    public Reclamation getReclamationById(int id) {
        String query = "SELECT * FROM reclamation WHERE id_rec = ?";
        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Reclamation(
                            rs.getInt("id_rec"),
                            rs.getInt("id_user"),
                            rs.getString("sujet"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getTimestamp("date_creation").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting reclamation by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}