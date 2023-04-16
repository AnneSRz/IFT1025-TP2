package com.example.clientfx;

import com.example.clientfx.modeleClientFx.RegistrationForm;
import com.example.clientfx.modeleClientFx.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Client_fxController implements Initializable {
    @FXML
    private AnchorPane pane;
    //<editor-fold desc="Attributs">
    @FXML
    private ChoiceBox choixSession;
    private String sessionVoulue;
    private ArrayList<Course> coursFiltres;
    @FXML
    private TableView<Course> coursesDisplay;
    @FXML
    private TableColumn<Course, String> colonneCode;
    @FXML
    private TableColumn<Course, String> colonneNom;
    @FXML
    private TextField prenomField, nomField, emailField, matriculeField;
    @FXML
    private Label emailError, matriculeError;
    @FXML
    private Button registrer, chargementButton;
    private ObservableList<Course> leCours;


    //</editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Step 1 : Se connecter au serveur;
        try {
            Socket socket = new Socket("127.0.0.1", 1337);

            this.sessionVoulue = (String) this.choixSession.getSelectionModel().getSelectedItem();
            if (sessionVoulue == null) {
                this.sessionVoulue = (String) this.choixSession.getSelectionModel().getSelectedItem();
            } else {
                this.sessionVoulue = (String) this.choixSession.getSelectionModel().getSelectedItem();
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
        // Step 1 : Aller chercher la session choisie dans la liste déroulante.
        try {
            // Step 1 : Se connecter au serveur
            Socket socket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Step 2 : Aller chercher la session choisie dans la liste déroulante.
            this.sessionVoulue = (String) this.choixSession.getSelectionModel().getSelectedItem();
            if (sessionVoulue == null) {
                this.sessionVoulue = (String) this.choixSession.getSelectionModel().getSelectedItem();

            } else {

                // Step 2 : Envoie une requete charger au serveur "Avec la session choisie"
                String requete = "CHARGER " + this.sessionVoulue;
                objectOutputStream.writeObject(requete);
                objectOutputStream.flush();
                objectOutputStream.reset();
                try {
                    // Step 3 : Le client recupere la liste des cours envoyes par le serveur"
                    this.coursFiltres = (ArrayList<Course>) objectInputStream.readObject();

                    // Step 4 : Affhiché la liste des cours dans le tableau
                    ObservableList<Course> listeDesCours = FXCollections.observableArrayList(this.coursFiltres);
                    this.coursesDisplay.setItems(listeDesCours);

                    this.colonneCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                    this.colonneNom.setCellValueFactory(new PropertyValueFactory<>("name"));

                    // Step 5 : Fermer le flux d'entrée et de sortie
                    objectOutputStream.close();
                    objectInputStream.close();
                    socket.close();

                    // Step 5 : Procéder à la partie de l'inscription
                    //handleRegistration();
                    this.registrer.setOnAction(this::handle);

                } catch (ClassNotFoundException e) {
                    System.out.print("Erreur lors du chargement la liste de cours n'a pas pu être affiché");
                    throw new RuntimeException(e);
                } catch (IOException ex) {
                    System.out.println("Erreur lors de l'envoie de la requête");
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //public void selection(ActionEvent actionEvent)

    @FXML
    public void handle(ActionEvent actionEvent) {
        if (prenomField.getText().isEmpty() && nomField.getText().isEmpty() && emailField.getText().isEmpty() &
                matriculeField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Le formulaire est invalide. Assurez-vous que tous les champs" +
                            " sont remplis");

            return;
        }
        if (prenomField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Entrez votre nom");

            return;
        }
        if (nomField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Entrez votre nom");
            return;
        }
        if (emailField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Entrez votre email");

            return;
        }
        if (matriculeField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Entrez votre matricule");

            return;
        }
        if (!emailValidation(emailField.getText()) || !matriculeValidation(matriculeField.getText())) {
            //boolean emailEstValide = validEmail(emailField, emailError, "Invalide, s'il vous plait réessayer");
            // boolean matriculeEstValide = validmatricule(matriculeField, matriculeError, "Invalide, s'il vous plait réessayer");
            emailError.setText("Email invalide");
            emailError.setStyle("-fx-text-fill: red");
            matriculeError.setText("Matricule invalide");
            matriculeError.setStyle("-fx-text-fill: red");
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Le champ email est invalide" + "\n" + "Le champ matricule est invalide");

        } else {
            emailError.setText("Email valide");
            emailError.setStyle("-fx-text-fill: green");
            matriculeError.setText("Matricule valide");
            matriculeError.setStyle("-fx-text-fill: green");

            handleRegistration();

            //this.registrer.setOnMouseClicked(this::selection);

        }
    }

    private void showAlert(Alert.AlertType error, Window window, String Message) {
        Alert messageAlert = new Alert(error);
        String messageErreur = "Message";
        messageAlert.setTitle(messageErreur);
        messageAlert.setContentText(Message);
        messageAlert.initOwner(window);
        messageAlert.show();
    }
    @FXML
    public void selection(MouseEvent mouseEvent) {
        this.leCours = this.coursesDisplay.getSelectionModel().getSelectedItems();
        System.out.println(leCours);
        if(this.leCours.isEmpty()){
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "S'il vous plait, veuillez sélectionner un cours");
        }else{
            this.registrer.setOnAction(this::handle);

        }
    }


    public void handleRegistration() {
        try {
            // Step 1 : Se reconnecter au serveur
            Socket socket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Step 2: Extraire les informations du tableaux
            ObservableList<Course> leCours = this.coursesDisplay.getSelectionModel().getSelectedItems();

            //ObservableList<Course> listeCours = FXCollections.observableArrayList(this.coursFiltres);

            for (Course coursSelectionne : leCours) {
                Course coursInscrit = new Course(coursSelectionne.getName(), coursSelectionne.getCode(), coursSelectionne.getSession());
                String code = coursInscrit.getCode();
                String cours = coursInscrit.getName();
                String session = coursInscrit.getSession();

                System.out.println(code);
                System.out.println(cours);
                System.out.println(session);


                String prenom = this.prenomField.getText();
                String nom = this.nomField.getText();
                String email = this.emailField.getText();
                String matricule = String.valueOf(matriculeField.getText());

                try {
                    RegistrationForm donneesInscription = new RegistrationForm(prenom, nom, email, matricule, coursSelectionne);
                    System.out.println(leCours);
                    // Step 5 : Envoyer une requete Inscription au server
                    String requeteInscription = "INSCRIRE";
                    objectOutputStream.writeObject(requeteInscription);
                    objectOutputStream.flush(); // jusqu'a la ça marche la requete est envoyé

                    //Step 6 : Envoyer l'objet Registrationform au serveur
                    objectOutputStream.writeObject(donneesInscription);
                    objectOutputStream.flush();

                    // Step 7 : Lire le message de confirmation du serveur.
                    Object msgConfirmation = objectInputStream.readObject();
                    String confirmation = (String) msgConfirmation;
                    showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), confirmation);
                    System.out.println(confirmation);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Une erreur s'est produite lors de l'envoie des données d'inscription");
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

            //ne pas oublier de traiter les exceptions si le fichiers de registration est mal formates.
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Step : Valider le Email
    private static boolean emailValidation(String text1) {
        return text1.matches("^[\\w!#$%&'*+/=?`{|}~^.-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^.-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    // Step : Valider le matricule
    private static boolean matriculeValidation(String text2) {
        return text2.matches("^[0-9]{8}$");
    }
}

