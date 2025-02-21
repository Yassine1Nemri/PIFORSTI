package com.melocode.videoismaael.Controller;

import com.melocode.videoismaael.entities.Reclamation;
import com.melocode.videoismaael.entities.User;
import com.melocode.videoismaael.services.ReclamationService;
import com.melocode.videoismaael.services.UserService;
import com.melocode.videoismaael.tools.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserReclamationController {
    @FXML private TextField tfSubject;
    @FXML private TextField tfIdReclamation; // Added missing field
    @FXML private TextField tfTitleReclamation; // Added missing field
    @FXML private TextField tfDescriptionReclamation; // Added missing field
    @FXML private TextField tfStatusReclamation; // Added missing field
    @FXML private TextArea taDescription;
    @FXML private ListView<String> listViewReclamations;

    private ReclamationService reclamationService;
    private User currentUser;

    public void initialize() {
        reclamationService = new ReclamationService();

        // Add selection listener
        listViewReclamations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Handle selection
                        showReclamationDetails(newValue);
                    }
                }
        );

        loadUserReclamations();
    }

    private void loadUserReclamations() {
        listViewReclamations.getItems().clear();
        String query = "SELECT * FROM reclamation";
        System.out.println("click");

        List<Reclamation> reclamations = reclamationService.getReclamationsByUser(1);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reclamation recInfo = new Reclamation(
                        rs.getString("sujet"),
                        rs.getString("status"),
                        rs.getString("date_creation")
                );

                // Format the reclamation info as a string
                String reclamationDisplay = String.format("ID: %d, Title: %s, Status: %s, Date: %s",
                        recInfo.getId(),
                        recInfo.getSujet(),
                        recInfo.getStatus(),
                        recInfo.getDate_creation()
                );

                listViewReclamations.getItems().add(reclamationDisplay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error loading reclamations: " + e.getMessage());
        }
    }

    @FXML
    private void handleSubmit() {
        if (validateInputs()) {
            int staticUserId = 1; // Set a static user ID

            Reclamation reclamation = new Reclamation(
                    staticUserId, // Using the static user ID
                    tfSubject.getText(),
                    taDescription.getText()
            );

            reclamationService.ajouterEntite(reclamation);
            clearForm();
            loadUserReclamations();
            showAlert("Success", "Reclamation submitted successfully!");
        }
    }


    @FXML
    private void handleClear() {
        clearForm();
    }

    private void clearForm() {
        tfSubject.clear();
        taDescription.clear();
    }

    private boolean validateInputs() {
        if (tfSubject.getText().isEmpty() || taDescription.getText().isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showReclamationDetails(String selectedItem) {
        if (selectedItem != null) {
            try {
                String[] parts = selectedItem.split(", ");
                String idPart = parts[0];
                int reclamationId = Integer.parseInt(idPart.split(": ")[1]);

                Reclamation reclamationCRUD = new Reclamation();
                Reclamation reclamation = reclamationCRUD.getReclamationById(reclamationId);

                if (reclamation != null) {
                    tfIdReclamation.setText(String.valueOf(reclamation.getId()));
                    tfTitleReclamation.setText(reclamation.getSujet());
                    tfDescriptionReclamation.setText(reclamation.getDescription());
                    tfStatusReclamation.setText(reclamation.getStatus());
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Error parsing reclamation details.");
            } catch (Exception e) {
                showAlert("Error", "An unexpected error occurred while loading reclamation details.");
            }
        }
    }
}