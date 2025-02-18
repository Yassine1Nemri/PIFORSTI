package com.melocode.videoismaael.services;

import com.melocode.videoismaael.entities.Tache;
import com.melocode.videoismaael.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TacheCRUD {
    private Connection connection;

    // 🔹 Initialisation de la connexion
    public TacheCRUD() {
        this.connection = DatabaseConnection.getConnection();
    }

    // 🔹 Ajouter une tâche dans la base de données
    public void ajouterTache(Tache t) {
        if (!projetExiste(t.getIdProjet())) {
            System.out.println("❌ Erreur : Le projet avec id " + t.getIdProjet() + " n'existe pas !");
            return;
        }

        String query = "INSERT INTO tache (idProjet, nomTache, description, statut) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, t.getIdProjet());
            pst.setString(2, t.getNomTache());
            pst.setString(3, t.getDescription());
            pst.setString(4, t.getStatut());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Tâche ajoutée avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de l'ajout de la tâche : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 🔹 Vérifier si un projet existe
    public boolean projetExiste(int idProjet) {
        String query = "SELECT COUNT(*) FROM projet WHERE idProjet = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idProjet);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la vérification du projet : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Récupérer toutes les tâches
    public List<Tache> getAllTaches() {
        List<Tache> taches = new ArrayList<>();
        String query = "SELECT * FROM tache";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Tache t = new Tache(
                        rs.getInt("idTache"),
                        rs.getString("nomTache"),
                        rs.getString("description"),
                        rs.getString("statut"),
                        rs.getInt("idProjet")
                );
                taches.add(t);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la récupération des tâches : " + e.getMessage());
            e.printStackTrace();
        }

        return taches;
    }

    // 🔹 Vérifier si une tâche existe
    public boolean tacheExists(int idTache) {
        String query = "SELECT COUNT(*) FROM tache WHERE idTache = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idTache);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la vérification de la tâche : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Modifier une tâche
    public boolean updateTache(Tache updatedTache) {
        if (updatedTache.getIdTache() <= 0) {
            System.out.println("❌ ID de tâche invalide !");
            return false;
        }

        if (!tacheExists(updatedTache.getIdTache())) {
            System.out.println("❌ La tâche avec ID " + updatedTache.getIdTache() + " n'existe pas !");
            return false;
        }

        String query = "UPDATE tache SET nomTache=?, description=?, statut=? WHERE idTache=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, updatedTache.getNomTache());
            pst.setString(2, updatedTache.getDescription());
            pst.setString(3, updatedTache.getStatut());
            pst.setInt(4, updatedTache.getIdTache());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Tâche modifiée avec succès !");
                return true;
            } else {
                System.out.println("❌ Échec de la modification !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la modification de la tâche : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Supprimer une tâche
    public boolean supprimerTache(int idTache) {
        if (!tacheExists(idTache)) {
            System.out.println("❌ La tâche avec ID " + idTache + " n'existe pas !");
            return false;
        }

        String query = "DELETE FROM tache WHERE idTache=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idTache);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Tâche supprimée avec succès !");
                return true;
            } else {
                System.out.println("❌ Échec de la suppression !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la suppression de la tâche : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
