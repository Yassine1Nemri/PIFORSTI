package com.melocode.videoismaael.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowProjet extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Projet.fxml"));
            AnchorPane root = loader.load();  // Charge le FXML

            // Créer la scène et l'afficher
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion des Projets");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);  // Lancer l'application JavaFX
    }
}
