package com.melocode.videoismaael.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpenGererProjet extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML pour l'interface de gestion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GererProjet.fxml"));
            System.out.println("Ouverture de la gestion des projets");
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Gestion des Projets et Tâches");
            primaryStage.setScene(scene); // Charger l'interface
            primaryStage.show(); // Afficher la fenêtre principale
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Lancer l'application
    }

}
