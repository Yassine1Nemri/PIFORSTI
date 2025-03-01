package com.melocode.videoismaael.Controller;

import com.melocode.videoismaael.entities.Reclamation;
import com.melocode.videoismaael.entities.User;
import com.melocode.videoismaael.services.ReclamationService;
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
import java.util.ResourceBundle;

public class UserReclamationController implements Initializable {
    @FXML private TextField tfSubject;
    @FXML private TextField tfIdReclamation;
    @FXML private TextField tfTitleReclamation;
    @FXML private TextField tfDescriptionReclamation;
    @FXML private TextField tfStatusReclamation;
    @FXML private TextArea taDescription;
    @FXML private ListView<String> listViewReclamations;

    private ReclamationService reclamationService;
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reclamationService = new ReclamationService();

        listViewReclamations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showReclamationDetails(newValue);
                    }
                }
        );

        loadUserReclamations();
    }

    private void loadUserReclamations() {
        listViewReclamations.getItems().clear();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reclamation");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String reclamationDisplay = String.format("ID: %d, Title: %s, Status: %s, Date: %s",
                        rs.getInt("id_rec"),
                        rs.getString("sujet"),
                        rs.getString("status"),
                        rs.getString("date_creation")
                );
                listViewReclamations.getItems().add(reclamationDisplay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading reclamations: " + e.getMessage());
        }
    }

    @FXML
    private void handleSubmit() {
        if (validateInputs()) {
            int staticUserId = 7;

            Reclamation reclamation = new Reclamation(
                    staticUserId,
                    tfSubject.getText(),
                    taDescription.getText()
            );

            reclamationService.ajouterEntite(reclamation);
            clearForm();
            loadUserReclamations();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reclamation submitted successfully!");
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
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showReclamationDetails(String selectedItem) {
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
            showAlert(Alert.AlertType.ERROR, "Error", "Error parsing reclamation details.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while loading reclamation details.");
        }
    }
}
