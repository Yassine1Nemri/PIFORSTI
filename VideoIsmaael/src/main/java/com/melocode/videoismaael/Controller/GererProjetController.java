package com.melocode.videoismaael.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GererProjetController {

    public Button login;
    @FXML
    private Button btnProjet;

    @FXML
    private Button btnTache;


    @FXML
    private void login() {
        try {
            // Charger le fichier FXML pour la gestion des projets
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface.fxml"));
            Stage stage = new Stage();
            stage.setTitle("login");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour ouvrir la gestion des projets
    @FXML
    private void openGestionProjet() {
        try {
            // Charger le fichier FXML pour la gestion des projets
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Projet.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestion des Projets");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ouvrir la gestion des tâches
    @FXML
    private void openGestionTache() {
        try {
            // Charger le fichier FXML pour la gestion des tâches
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tache.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestion des Tâches");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUserReclamation() {
        try {
            // Charger le fichier FXML pour la gestion des réclamations
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserReclamation.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Faire une réclamation");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openReclamation(ActionEvent actionEvent) {

        try {
            // Load the User reclamation FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserReclamation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("User Reclamation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openadminreclamation(ActionEvent actionEvent) {
        try {
            // Load the FXML file for admin reclamation interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminResponse.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Admin Reclamation");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
