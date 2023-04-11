package com.example.clientfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Client_fxController {
    @FXML
    private Label welcomeText;

    // Qu'est- ce qui apparait lorsqu'on clique sur un bouton par exemple
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}