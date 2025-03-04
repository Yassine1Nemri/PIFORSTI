package com.melocode.videoismaael.Controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    private VBox menuVBox;

    @FXML
    private Pane contenuPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisez votre contrôleur ici si nécessaire
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface.fxml"));
            Pane interfacePane = loader.load();
            contenuPane.getChildren().setAll(interfacePane.lookup("#pn_update"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherOption1() {
        // Chargez le contenu de l'option 1 dans la zone de droite
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface.fxml"));
            Pane interfacePane = loader.load();
            contenuPane.getChildren().setAll(interfacePane.lookup("#pn_update"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadContent(String fxmlFileName) {
        try {
            AnchorPane content = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
            contenuPane.getChildren().setAll(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
