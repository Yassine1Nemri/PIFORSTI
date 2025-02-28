package com.melocode.videoismaael.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UpdateController {

    @FXML
    private TextField tf_fn1, tf_ln1, tf_num1, tf_email1, tf_pass1;

    // Handle update logic here
    @FXML
    private void update() {
        // Fetch values from the fields and process them as needed
        String firstName = tf_fn1.getText();
        String lastName = tf_ln1.getText();
        String phoneNumber = tf_num1.getText();
        String email = tf_email1.getText();
        String password = tf_pass1.getText();

        // Update logic (e.g., save data to database, etc.)
        System.out.println("Updated: " + firstName + " " + lastName);
    }
}
