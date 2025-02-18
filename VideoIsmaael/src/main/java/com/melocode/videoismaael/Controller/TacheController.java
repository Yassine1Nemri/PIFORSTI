package com.melocode.videoismaael.Controller;

import com.melocode.videoismaael.entities.Tache;
import com.melocode.videoismaael.services.TacheCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.regex.Pattern;

public class TacheController {

    // Champs de saisie pour les informations d'une tâche
    @FXML
    private TextField tfidTache, tfnomTache, tfdescription, tfstatut, tfidProjet;

    // Boutons pour interagir avec l'interface
    @FXML
    private Button btnValider, btnModifier, btnSupprimer, btnAfficher;

    // Liste des tâches affichées
    @FXML
    private ListView<String> listViewTaches;

    // Instance de TacheCRUD pour gérer la base de données
    private final TacheCRUD tacheCRUD = new TacheCRUD();

    // 🔹 Méthode pour afficher toutes les tâches dans la ListView
    public void displayTaches(ActionEvent actionEvent) {
        List<Tache> taches = tacheCRUD.getAllTaches();
        listViewTaches.getItems().clear();  // Effacer la liste avant d'ajouter les nouvelles tâches

        for (Tache tache : taches) {
            listViewTaches.getItems().add(tache.toString()); // Affichage sous forme de chaîne de caractères
        }
    }

    // 🔹 Vérifier si le nom de la tâche est valide (lettres et espaces uniquement)
    private boolean validateNomTache(String nomTache) {
        String regex = "^[a-zA-Z\\s]+$";
        return Pattern.matches(regex, nomTache);
    }

    // 🔹 Ajouter une nouvelle tâche
    public void saveTache(ActionEvent actionEvent) {
        try {
            // Vérifier si les champs sont remplis
            if (tfnomTache.getText().trim().isEmpty() || tfdescription.getText().trim().isEmpty() ||
                    tfstatut.getText().trim().isEmpty() || tfidProjet.getText().trim().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
                return;
            }

            // Récupérer les valeurs des champs
            String nomTache = tfnomTache.getText().trim();
            if (!validateNomTache(nomTache)) {
                showAlert("Erreur", "Le nom de la tâche doit contenir uniquement des lettres.", Alert.AlertType.ERROR);
                return;
            }

            String description = tfdescription.getText().trim();
            String statut = tfstatut.getText().trim();
            int idProjet = Integer.parseInt(tfidProjet.getText().trim());

            // Vérifier si le projet existe
            if (!tacheCRUD.projetExiste(idProjet)) {
                showAlert("Erreur", "Le projet avec ID " + idProjet + " n'existe pas.", Alert.AlertType.ERROR);
                return;
            }

            // Créer la tâche et l'ajouter
            Tache tache = new Tache(0, nomTache, description, statut, idProjet);
            tacheCRUD.ajouterTache(tache);

            // Rafraîchir l'affichage
            displayTaches(actionEvent);
            showAlert("Succès", "Tâche ajoutée avec succès.", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID du projet doit être un nombre.", Alert.AlertType.ERROR);
        }
    }

    // 🔹 Modifier une tâche existante
    public void updateTache(ActionEvent actionEvent) {
        try {
            // Vérifier si les champs sont remplis
            if (tfidTache.getText().trim().isEmpty() || tfnomTache.getText().trim().isEmpty() ||
                    tfdescription.getText().trim().isEmpty() || tfstatut.getText().trim().isEmpty() ||
                    tfidProjet.getText().trim().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
                return;
            }

            // Récupérer les valeurs des champs
            int idTache = Integer.parseInt(tfidTache.getText().trim());
            String nomTache = tfnomTache.getText().trim();
            String description = tfdescription.getText().trim();
            String statut = tfstatut.getText().trim();
            int idProjet = Integer.parseInt(tfidProjet.getText().trim());

            // Vérifier si la tâche existe
            if (!tacheCRUD.tacheExists(idTache)) {
                showAlert("Erreur", "La tâche avec ID " + idTache + " n'existe pas.", Alert.AlertType.ERROR);
                return;
            }

            // Vérifier si le projet existe
            if (!tacheCRUD.projetExiste(idProjet)) {
                showAlert("Erreur", "Le projet avec ID " + idProjet + " n'existe pas.", Alert.AlertType.ERROR);
                return;
            }

            // Modifier la tâche
            Tache tacheModifiee = new Tache(idTache, nomTache, description, statut, idProjet);
            boolean success = tacheCRUD.updateTache(tacheModifiee);

            if (success) {
                displayTaches(actionEvent);
                showAlert("Succès", "Tâche modifiée avec succès.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Échec de la modification.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID de la tâche et l'ID du projet doivent être des nombres.", Alert.AlertType.ERROR);
        }
    }

    // 🔹 Supprimer une tâche
    public void deleteTache(ActionEvent actionEvent) {
        try {
            // Vérifier si le champ ID est rempli
            if (tfidTache.getText().trim().isEmpty()) {
                showAlert("Erreur", "Veuillez saisir l'ID de la tâche à supprimer.", Alert.AlertType.ERROR);
                return;
            }

            int idTache = Integer.parseInt(tfidTache.getText().trim());

            // Vérifier si la tâche existe
            if (!tacheCRUD.tacheExists(idTache)) {
                showAlert("Erreur", "La tâche avec ID " + idTache + " n'existe pas.", Alert.AlertType.ERROR);
                return;
            }

            // Supprimer la tâche
            boolean success = tacheCRUD.supprimerTache(idTache);
            if (success) {
                displayTaches(actionEvent);
                showAlert("Succès", "Tâche supprimée avec succès.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "La suppression a échoué.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID de la tâche doit être un nombre.", Alert.AlertType.ERROR);
        }
    }

    // 🔹 Méthode pour afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
