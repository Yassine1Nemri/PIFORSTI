package com.melocode.videoismaael.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
}
