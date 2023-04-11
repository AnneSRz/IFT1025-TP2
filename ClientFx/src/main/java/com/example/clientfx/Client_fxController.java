package com.example.clientfx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.example.clientfx.modeleClientFx.Course;
import com.example.clientfx.modeleClientFx.RegistrationForm;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class Client_fxController implements Initializable {
    private Socket socket;
    private Course module1;
    private RegistrationForm module2;
    @FXML
    private TableView<Course> coursesDisplay;
    @FXML
    private MenuButton coursLists;
    @FXML
    private Button chargementButton, registrer;
    @FXML
    private Label emailError, matriculeError;
    @FXML
    private TextField emailField, matriculeField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Step 1 : Se connecter au serveur;
        try{
            this.socket = new Socket("127.0.0.1",1337);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> coursCol = new TableColumn<>("Cours");
        coursCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        coursesDisplay.getColumns().addAll(codeCol, coursCol);
        //coursesDisplay.getItems().addAll(new Course("code1", "cours1"), new Course("code2", "cours2"));
    }

    @FXML
    public void handleCharge() {
        System.out.println("Chargement des cours");
    }

    public void selection(ActionEvent actionEvent) {
    }

    @FXML
    public void handleRegistration() {
        //ne pas oublier de traiter les exceptions si le fichiers de registration est mal formates.

        // Step : Validder que l'email et le matricule  sont conformes avant d'envoyer le formulaire
        boolean emailEstValide = validEmail(emailField, emailError, "Invalide, s'il vous plait réessayer");
        boolean matriculeEstValide = validmatricule(matriculeField, matriculeError, "Invalide, s'il vous plait réessayer");
        if (emailEstValide && matriculeEstValide) {
            emailError.setText("Email Valide");
            matriculeError.setText("Matricule Valide");
        }
        // Step : S'assurer que le matricule respecte le format avant d'envoyer le formulaire

        System.out.println("Envoi des données du formulaire");
    }

    // Step : Valider le Email
    private static boolean emailValidation(String text1) {
        return text1.matches("^[\\w!#$%&'*+/=?`{|}~^.-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^.-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    private static boolean validEmail(TextField textField1, Label error, String msgErreur) {
        boolean mail = emailValidation(textField1.getText());
        if (!mail) {
            textField1.getStyleClass().remove("invalide");
            error.setText(msgErreur);
            textField1.getStyleClass().add("invalide");
        } else{
            error.setText(null);
        }
        return mail;
    }

    // Step : Valider le matricule
    private static boolean matriculeValidation(String text2) {
        return text2.matches("^[0-9]{8}$");
    }
    private static boolean validmatricule(TextField textField2, Label erreur, String erreurMsg) {
        boolean matricule = matriculeValidation(textField2.getText());
        textField2.getStyleClass().remove("invalide");
        if (!matricule) {
            erreur.setText(erreurMsg);
            textField2.getStyleClass().add("invalide");
        } else {
            erreur.setText(null);
        }
        return matricule;
    }
}



// Ajouter les choix a la liste deroulante "choix de session"

    // Action pour envoyer

    /*
    submitButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if(nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                        "Form Error!", "Please enter your name");
                return;
            }
            if(emailField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                        "Form Error!", "Please enter your email id");
                return;
            }
            if(passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                        "Form Error!", "Please enter a password");
                return;
            }

            showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(),
                    "Registration Successful!", "Welcome " + nameField.getText());
        }
    });

     */
