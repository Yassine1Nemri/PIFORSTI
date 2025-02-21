package com.melocode.videoismaael.Controller;
import com.melocode.videoismaael.entities.Reclamation;
import com.melocode.videoismaael.entities.Response;
import com.melocode.videoismaael.services.ReclamationService;
import com.melocode.videoismaael.services.ResponseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

public class AdminResponseController implements Initializable {
    @FXML
    private ListView<String> listViewPendingReclamations;
    @FXML private TextArea taReclamationContent;
    @FXML private TextArea taResponse;
    @FXML private ComboBox<String> cbStatus;

    private ReclamationService reclamationService;
    private ResponseService responseService;
    private Reclamation selectedReclamation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reclamationService = new ReclamationService();
        responseService = new ResponseService();

        cbStatus.getItems().addAll("pending", "in-progress", "resolved");

        // Add selection listener
        listViewPendingReclamations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        handleReclamationSelection(newValue);
                    }
                }
        );

        loadPendingReclamations();
    }
    private int getCurrentAdminId() {
        // Implement this method to return the current admin's ID
        // This is a placeholder - replace with actual implementation
        return 1;
    }

    @FXML
    private void handleSendResponse() {
        if (validateResponse()) {
            Response response = new Response(
                    selectedReclamation.getId_rec(),
                    getCurrentAdminId(),
                    taResponse.getText()
            );

            responseService.ajouterEntite(response);

            // Update reclamation status
            selectedReclamation.setStatus(cbStatus.getValue());
            reclamationService.modifierEntite(selectedReclamation);

            clearForm();
            loadPendingReclamations();
            showAlert("Success", "Response sent successfully!");
        }
    }

    private void loadPendingReclamations() {
        listViewPendingReclamations.getItems().clear();
        List<Reclamation> reclamations = reclamationService.afficherEntite();

        for (Reclamation rec : reclamations) {
            String recInfo = String.format("ID: %d | Subject: %s | User: %d | Status: %s",
                    rec.getId_rec(),
                    rec.getSujet(),
                    rec.getId_user(),
                    rec.getStatus()
            );
            listViewPendingReclamations.getItems().add(recInfo);
        }
    }

    private void handleReclamationSelection(String reclamationInfo) {
        // Extract reclamation ID from the selected item
        int recId = extractReclamationId(reclamationInfo);
        selectedReclamation = reclamationService.getReclamationById(recId);

        if (selectedReclamation != null) {
            taReclamationContent.setText(selectedReclamation.getDescription());
            cbStatus.setValue(selectedReclamation.getStatus());
        }
    }

    private void clearForm() {
        taResponse.clear();
        taReclamationContent.clear();
        selectedReclamation = null;
    }

    private boolean validateResponse() {
        if (selectedReclamation == null || taResponse.getText().isEmpty()) {
            showAlert("Error", "Please select a reclamation and write a response!");
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

    private int extractReclamationId(String reclamationInfo) {
        // Extract ID from the format "ID: X | Subject: ..."
        String idPart = reclamationInfo.split("\\|")[0].trim();
        return Integer.parseInt(idPart.replace("ID: ", ""));
    }
}