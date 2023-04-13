package com.example.clientfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import com.example.clientfx.modeleClientFx.Course;
import com.example.clientfx.modeleClientFx.RegistrationForm;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Client_fxController implements Initializable {
    //<editor-fold desc="Attributs">
    @FXML
    private ChoiceBox choixSession;
    private String session;
    private ArrayList<Course> coursFiltres;
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
    //</editor-fold>


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Step 1 : Se connecter au serveur;
        try {
            Socket socket = new Socket("127.0.0.1",1337);

            this.session = (String) this.choixSession.getSelectionModel().getSelectedItem();
            if(session == null){
                this.session = (String) this.choixSession.getSelectionModel().getSelectedItem();
            }else{
                this.session = (String) this.choixSession.getSelectionModel().getSelectedItem();
                handleCharge();
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Une erreur s'est produite lors de la connesion au serveur");
        }
    }
    @FXML
    public void handleCharge() {
        //Step 1 : Aller chercher la session choisie dans la liste déroulante.
        try {
            this.session = (String) this.choixSession.getSelectionModel().getSelectedItem();

            Socket socket = new Socket("127.0.0.1",1337);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(session);
            System.out.println("Chargement des cours");

            //Step 2 : Envoie une requete charger au serveur "Avec la session choisie"
            String requete = "CHARGER " + this.session;
            objectOutputStream.writeObject(requete);
            objectOutputStream.flush();
            try{
                //Step 3 : Le client recupere la liste des cours envoyes par le serveur"
                this.coursFiltres = (ArrayList) objectInputStream.readObject() ;

                //Step 4 : Affhiché la liste des cours dans le tableau
                for (int i = 0; i < coursFiltres.size(); i++ ){
                    System.out.println((i+1)+". " + coursFiltres.get(i).getCode() + " " + coursFiltres.get(i).getName());
                }
                objectOutputStream.close();
                objectInputStream.close();
            } catch (ClassNotFoundException e) {
                System.out.print("Erreur lors du chargement la liste de cours n'a pas pu être affiché");
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'envoie de la requête");
        }
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
     TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> coursCol = new TableColumn<>("Cours");
        coursCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        coursesDisplay.getColumns().addAll(codeCol, coursCol);
        //coursesDisplay.getItems().addAll(new Course("code1", "cours1"), new Course("code2", "cours2"));

     */
