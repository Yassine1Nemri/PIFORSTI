package com.melocode.videoismaael.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.melocode.videoismaael.entities.User;
import com.melocode.videoismaael.services.UserService;

public class UserC {

    @FXML
    private Label ban;

    @FXML
    private Label mail;

    @FXML
    private Label name;

    @FXML
    private Label tel;

    @FXML
    private Button deleteButton;

    private User user;

    UserService us = new UserService();

    public void setData(User q) {
        this.user = q;
        name.setText("Name : " + q.getName() + " " + q.getPrenom());
        mail.setText("Email :" + q.getEmail());
        tel.setText("Phone : " + String.valueOf(q.getTel()));
        if (q.getIs_banned() != 0) {
            ban.setText("this user is banned");
        } else {
            ban.setText("This user account isn't banned");
        }
    }

    public void setUser(User user) {
        this.user = user;
        name.setText(user.getName());
        mail.setText(user.getEmail());
        tel.setText(String.valueOf(user.getTel()));
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        if (user != null) {
            us.supprimerEntite(user);
            System.out.println("User deleted successfully: " + user.getId());
        }
    }
}
