package com.melocode.videoismaael.Controller;

import com.melocode.videoismaael.entities.Tache;
import com.melocode.videoismaael.services.TacheCRUD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowTache extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML pour la gestion des tâches
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tache.fxml"));
            AnchorPane root = loader.load();  // Charge le FXML pour Tache

            // Créer la scène et l'afficher
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion des Tâches");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


public static void main(String[] args) {
    TacheCRUD tacheCRUD = new TacheCRUD();

    // Vérifie si un projet existe, sinon en crée un
    int idProjet = 1; // Mets ici un id existant
    if (!tacheCRUD.projetExiste(idProjet)) {
        System.out.println("⚠ Aucun projet trouvé, veuillez en ajouter un dans la base de données.");
        return;
    }

    // Ajouter une tâche
    Tache t1 = new Tache(0, "Développement", "Coder l'application", "En cours", idProjet);
    tacheCRUD.ajouterTache(t1);
}}
