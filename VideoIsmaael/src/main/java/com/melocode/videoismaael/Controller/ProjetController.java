package com.melocode.videoismaael.Controller;

import com.melocode.videoismaael.entities.Projet;
import com.melocode.videoismaael.services.ProjetCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class ProjetController implements Initializable {

    @FXML
    private TextField tfidProjet, tfnomProjet, tfdateDebut, tfdateFin, tfstatut;

    @FXML
    private Button btnValider, btnSupprimer, btnModifier, btnAfficher;
    @FXML
    private ListView<String> listViewProjets;  // Liste des projets


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation si nécessaire

        // Ajouter un listener pour gérer la sélection dans la ListView
        listViewProjets.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Extraire l'ID du projet sélectionné
                int idProjet = extractProjectId(newValue);
                // Remplir les champs du formulaire avec les données du projet sélectionné
                fillFormWithProject(idProjet);
            }
        });
    }

    // Fonction pour extraire l'ID du projet à partir de la chaîne de texte de la ListView
    private int extractProjectId(String projetInfo) {
        String[] parts = projetInfo.split(", ");
        String idPart = parts[0]; // "ID: <id>"
        String idStr = idPart.split(": ")[1]; // Extraire l'ID
        return Integer.parseInt(idStr);
    }

    // Fonction pour remplir les champs du formulaire avec les informations du projet sélectionné
    private void fillFormWithProject(int idProjet) {
        ProjetCRUD pcr = new ProjetCRUD();
        Projet projet = pcr.getProjectById(idProjet);

        if (projet != null) {
            tfidProjet.setText(String.valueOf(projet.getIdProjet()));
            tfnomProjet.setText(projet.getNomProjet());
            tfdateDebut.setText(projet.getDateDebut().toString());
            tfdateFin.setText(projet.getDateFin().toString());
            tfstatut.setText(projet.getStatut());
        }
    }

    @FXML
    public void saveProject(ActionEvent actionEvent) {
        try {
            // Récupérer les données du formulaire
            int idProjet = Integer.parseInt(tfidProjet.getText().trim());
            String nomProjet = tfnomProjet.getText().trim();

            // Vérifier si nomProjet contient uniquement des lettres
            if (!nomProjet.matches("[a-zA-Z]+")) {
                showAlert("Erreur", "Le nom du projet doit être composé uniquement de lettres.", Alert.AlertType.ERROR);
                return;
            }

            String dateDebutStr = tfdateDebut.getText().trim();
            String dateFinStr = tfdateFin.getText().trim();

            // Vérification des dates
            if (!isValidDate(dateDebutStr) || !isValidDate(dateFinStr)) {
                showAlert("Erreur", "Les dates doivent être au format YYYY-MM-DD (ex: 2025-02-08).", Alert.AlertType.ERROR);
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateDebut = LocalDate.parse(dateDebutStr, formatter);
            LocalDate dateFin = LocalDate.parse(dateFinStr, formatter);

            String statut = tfstatut.getText().trim();

            // Vérifier si l'ID du projet existe déjà
            ProjetCRUD pcr = new ProjetCRUD();
            boolean idExist = pcr.getAllProjects().stream()
                    .anyMatch(projet -> projet.getIdProjet() == idProjet);

            if (idExist) {
                showAlert("Erreur", "L'ID du projet existe déjà. Veuillez entrer un ID unique.", Alert.AlertType.ERROR);
                return;
            }

            // Création d'un nouvel objet Projet
            Projet p = new Projet(idProjet, nomProjet, dateDebut, dateFin, statut);

            // Appeler la méthode pour ajouter le projet
            pcr.ajouterProjet(p);
            showAlert("Succès", "Projet ajouté avec succès !", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID du projet doit être un nombre.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue s'est produite.", Alert.AlertType.ERROR);
        }
    }

    // Fonction pour afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Fonction de validation de la date
    private boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Suppression d'un projet
    @FXML
    public void deleteProject(ActionEvent actionEvent) {
        try {
            int idProjet = Integer.parseInt(tfidProjet.getText().trim());
            ProjetCRUD pcr = new ProjetCRUD();

            boolean success = pcr.supprimerProjet(idProjet);
            if (success) {
                showAlert("Succès", "Projet supprimé avec succès !", Alert.AlertType.INFORMATION);
                // Rafraîchir la liste des projets
                displayProjects(actionEvent);
            } else {
                showAlert("Erreur", "Le projet avec cet ID n'existe pas.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID du projet doit être un nombre.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void updateProject(ActionEvent actionEvent) {
        try {
            int idProjet = Integer.parseInt(tfidProjet.getText().trim());  // ID du projet à modifier
            String nomProjet = tfnomProjet.getText().trim();
            String dateDebutStr = tfdateDebut.getText().trim();
            String dateFinStr = tfdateFin.getText().trim();
            String statut = tfstatut.getText().trim();

            // Vérifier si les dates sont valides avant de les convertir
            if (!isValidDate(dateDebutStr) || !isValidDate(dateFinStr)) {
                showAlert("Erreur", "Les dates doivent être au format YYYY-MM-DD (ex: 2025-02-08).", Alert.AlertType.ERROR);
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateDebut = LocalDate.parse(dateDebutStr, formatter);
            LocalDate dateFin = LocalDate.parse(dateFinStr, formatter);

            // Modifier le projet en base de données
            ProjetCRUD pcr = new ProjetCRUD();
            Projet projetModifie = new Projet(idProjet, nomProjet, dateDebut, dateFin, statut);

            boolean success = pcr.modifierProjet(idProjet, projetModifie);
            if (success) {
                showAlert("Succès", "Le projet a été modifié avec succès.", Alert.AlertType.INFORMATION);
                displayProjects(actionEvent); // Rafraîchir la liste
            } else {
                showAlert("Erreur", "Le projet avec cet ID n'existe pas.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID du projet doit être un nombre.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue s'est produite.", Alert.AlertType.ERROR);
        }
    }


    // Affichage de tous les projets
    @FXML
    public void displayProjects(ActionEvent actionEvent) {
        ProjetCRUD pcr = new ProjetCRUD();
        List<Projet> projets = pcr.getAllProjects();

        // Vider le ListView avant d'ajouter les projets
        listViewProjets.getItems().clear();

        // Ajouter les projets dans le ListView
        for (Projet projet : projets) {
            String projetInfo = "ID: " + projet.getIdProjet() + ", "
                    + "Nom: " + projet.getNomProjet() + ", "
                    + "Date Début: " + projet.getDateDebut() + ", "
                    + "Date Fin: " + projet.getDateFin() + ", "
                    + "Statut: " + projet.getStatut();
            listViewProjets.getItems().add(projetInfo);
        }
    }
}
