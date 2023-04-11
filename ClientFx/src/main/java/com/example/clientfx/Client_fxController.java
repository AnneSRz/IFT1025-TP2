package com.example.clientfx;

import com.example.clientfx.modeleClientFx.Course;
import com.example.clientfx.modeleClientFx.RegistrationForm;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class Client_fxController {
    private Client_fxView vue;
    private Course module1;
    private RegistrationForm module2;
    @FXML
    private Label chargerText;
    @FXML
    private Label envoyerText;

    // Qu'est- ce qui apparait lorsqu'on clique sur un bouton par exemple
    //Loraqu'on clique sur charger, la liste des cours filtres apparait
    @FXML
    protected void onChargerButtonClick() {chargerText.setText("Welcome to JavaFX Application!");
    }
    protected void onEnvoyerButtonClick() {envoyerText.setText("Welcome to JavaFX Application!");
    }

}