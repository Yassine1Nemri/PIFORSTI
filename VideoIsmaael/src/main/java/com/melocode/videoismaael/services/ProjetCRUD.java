package com.melocode.videoismaael.services;

import com.melocode.videoismaael.entities.Projet;
import com.melocode.videoismaael.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetCRUD {
    private Connection connection;

    public ProjetCRUD() {
        this.connection = DatabaseConnection.getConnection();
    }

    // ✅ Ajouter un projet dans MySQL
    public void ajouterProjet(Projet projet) {
        String query = "INSERT INTO projet (nomProjet, dateDebut, dateFin, statut) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, projet.getNomProjet());
            pst.setDate(2, Date.valueOf(projet.getDateDebut())); // Conversion String -> SQL Date
            pst.setDate(3, Date.valueOf(projet.getDateFin()));
            pst.setString(4, projet.getStatut());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Projet ajouté avec succès !");
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    projet.setIdProjet(rs.getInt(1)); // Récupérer l'ID généré
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Récupérer tous les projets depuis MySQL
    public List<Projet> getAllProjects() {
        List<Projet> projets = new ArrayList<>();
        String query = "SELECT * FROM projet";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Projet projet = new Projet(
                        rs.getInt("idProjet"),
                        rs.getString("nomProjet"),
                        rs.getDate("dateDebut").toLocalDate(),
                        rs.getDate("dateFin").toLocalDate(),
                        rs.getString("statut")
                );
                projets.add(projet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projets;
    }

    // ✅ Supprimer un projet par ID
    public boolean supprimerProjet(int idProjet) {

        String query = "DELETE FROM projet WHERE idProjet = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idProjet);
            int rowsDeleted = pst.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Modifier un projet
    public boolean modifierProjet(int idProjet, Projet projetModifie) {
        String query = "UPDATE projet SET nomProjet = ?, dateDebut = ?, dateFin = ?, statut = ? WHERE idProjet = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, projetModifie.getNomProjet());
            pst.setDate(2, Date.valueOf(projetModifie.getDateDebut()));
            pst.setDate(3, Date.valueOf(projetModifie.getDateFin()));
            pst.setString(4, projetModifie.getStatut());
            pst.setInt(5, idProjet); // L'ID du projet à modifier

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Projet modifié avec succès !");
                return true;
            } else {
                System.out.println("❌ Aucun projet trouvé avec l'ID " + idProjet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // ✅ Récupérer un projet par ID
    public Projet getProjectById(int idProjet) {
        String query = "SELECT * FROM projet WHERE idProjet = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idProjet);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Projet(
                        rs.getInt("idProjet"),
                        rs.getString("nomProjet"),
                        rs.getDate("dateDebut").toLocalDate(),
                        rs.getDate("dateFin").toLocalDate(),
                        rs.getString("statut")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
