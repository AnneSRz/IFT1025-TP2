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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Client_fxController implements Initializable {
    //<editor-fold desc="Attributs">
    @FXML
    private AnchorPane pane;
    @FXML
    private ChoiceBox choixSession;
    private String sessionVoulue;
    @FXML
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
    private Button registrer;
    @FXML
    private ObservableList<Course> leCours;
    private Course coursSelectionne;
    //</editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Step 1 : Se connecter au serveur;
        try {
            Socket socket = new Socket("127.0.0.1", 1337);

            // Step 2 ; Verifier qu'une session a été choisie
            this.sessionVoulue = (String) this.choixSession.getSelectionModel().getSelectedItem();
            if (sessionVoulue == null) {
                this.sessionVoulue = (String) this.choixSession.getSelectionModel().getSelectedItem();
            } else {
                // Step 3 : Procéder à charger les cours si une session est choisie
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
                try {
                    // Step 3 : Envoie une requete charger au serveur "Avec la session choisie"
                    String requete = "CHARGER " + this.sessionVoulue;
                    objectOutputStream.writeObject(requete);
                    objectOutputStream.flush();
                    objectOutputStream.reset();

                    // Step 4 : Le client recupere la liste des cours envoyes par le serveur
                    this.coursFiltres = (ArrayList<Course>) objectInputStream.readObject();

                    // Step 5 : Affhiché la liste des cours dans le tableau
                    ObservableList<Course> listeDesCours = FXCollections.observableArrayList(this.coursFiltres);
                    this.coursesDisplay.setItems(listeDesCours);

                    this.colonneCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                    this.colonneNom.setCellValueFactory(new PropertyValueFactory<>("name"));

                    // Step 6 : Fermer le socket et les fluxs d'entrées et de sortie
                    objectOutputStream.close();
                    objectInputStream.close();
                    socket.close();

                    // Step 7 : S'occuper des execeptions rencontrés lorsque le bouton Registrer est appuyé.
                    this.registrer.setOnAction(this::handle);

                } catch (ClassNotFoundException e) {
                    System.out.print("Erreur lors du chargement la liste de cours n'a pas pu être affiché");
                    throw new RuntimeException(e);
                } catch (IOException ex) {
                    System.out.println("Erreur lors de l'envoie de la requête");
                }
            }
        } catch (UnknownHostException e) {
            System.out.println();
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("La connection au serveur n'a pas pu s'établir");
            throw new RuntimeException(e);
        }
    }

    // Valider le Email qui respecte le bon format
    private static boolean emailValidation(String text1) {
        return text1.matches("^[\\w!#$%&'*+/=?`{|}~^.-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^.-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    // Valider le matricule que le matricule respecte le bon format
    private static boolean matriculeValidation(String text2) {
        return text2.matches("^[0-9]{8}$");
    }

    @FXML
    public void handle(ActionEvent actionEvent) {
        // Step 1 : Un cours doit être sélectionné dans le tableau
        this.leCours =  this.coursesDisplay.getSelectionModel().getSelectedItems();
        System.out.println(leCours);
        if(this.leCours == null){
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Le formulaire est invalide" + "\n" + "Vous devez sélectionner un cours");
        }
        // Step 2 : Les champs ne doivent pas êtres vide
        if (prenomField.getText().isEmpty() && nomField.getText().isEmpty() && emailField.getText().isEmpty() &
                matriculeField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Le formulaire est invalide" +"\n" +" Assurez-vous que tous les champs sont remplis");
            return;
        }

        if (prenomField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), "Entrez votre prenom");

            return;
        }
        if (nomField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), "Entrez votre nom");
            return;
        }
        if (emailField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), "Entrez votre email");

            return;
        }
        if (matriculeField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), "Entrez votre matricule");

            return;
        }
        // Step 3 : Le amail et le matricule doivent être valide
        if(!emailValidation(emailField.getText()) && matriculeValidation(matriculeField.getText())){
            emailError.setText("Email invalide");
            emailError.setStyle("-fx-text-fill: red" );
            emailField.setStyle("-fx-border-color:red ; -fx-border-width: 0.5px");

            matriculeError.setText("Matricule valide");
            matriculeError.setStyle("-fx-text-fill: green");
            matriculeField.setStyle("-fx-border-color: transparent");
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Le formulaire est invalide" + "\n" +"Le champ email est invalide");
            return;
        }
        if(!matriculeValidation(matriculeField.getText()) && emailValidation(emailField.getText())){
            matriculeError.setText("Matricule invalide");
            matriculeError.setStyle("-fx-text-fill: red");
            matriculeField.setStyle("-fx-border-color:red ; -fx-border-width: 0.5px");

            emailError.setText("Email valide");
            emailError.setStyle("-fx-text-fill: green");
            emailField.setStyle("-fx-border-color: transparent");

            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Le formulaire est invalide"  + "\n" + "Le champ matricule est invalide");
            return;
        }
        if (!emailValidation(emailField.getText()) && !matriculeValidation(matriculeField.getText())) {
            emailError.setText("Email invalide");
            emailError.setStyle("-fx-text-fill: red" );
            emailField.setStyle("-fx-border-color:red ; -fx-border-width: 0.5px");

            matriculeError.setText("Matricule invalide");
            matriculeError.setStyle("-fx-text-fill: red");
            matriculeField.setStyle("-fx-border-color:red ; -fx-border-width: 0.5px");
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),
                    "Le formulaire est invalide" + "\n" +"Le champ email est invalide" + "\n" +
                            "Le champ matricule est invalide");

        } // Si les conditions sont valide, procédés à l'inscription
        else {
            emailError.setText("Email valide");
            emailError.setStyle("-fx-text-fill: green");
            emailField.setStyle("-fx-border-color: transparent");

            matriculeError.setText("Matricule valide");
            matriculeError.setStyle("-fx-text-fill: green");
            matriculeField.setStyle("-fx-border-color: transparent");

            handleRegistration();

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

    public void handleRegistration() {
        RegistrationForm donneesInscription = null;
        try {
            // Step 1 : Se reconnecter au serveur
            Socket socket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Step 2: Extraire les informations du cours sélectionné du tableaux
            for (Course coursTemp : this.leCours) {
                String nomCours = coursTemp.getName();
                String codeCours = coursTemp.getCode();
                String sessionCours = coursTemp.getSession();

                this.coursSelectionne = new Course(nomCours,codeCours,sessionCours);

            }
            // Step 3 : Obtenir les données relatifs au formulaire d'inscription entrées par l'utilisateur

            String prenom = prenomField.getText();
            String nom = nomField.getText();
            String email = emailField.getText();
            String matricule = matriculeField.getText();

            try {
                donneesInscription = new RegistrationForm(prenom, nom, email, matricule, coursSelectionne);
                System.out.print(donneesInscription);

                // Step 4 : Envoyer une requete Inscription au server
                String requeteInscription = "INSCRIRE";
                objectOutputStream.writeObject(requeteInscription);
                objectOutputStream.flush(); // jusqu'a la ça marche la requete est envoyé

                //Step 5 : Envoyer l'objet Registrationform au serveur
                objectOutputStream.writeObject(donneesInscription);
                objectOutputStream.flush();

                // Step 6 : Lire le message de confirmation du serveur.
                Object msgConfirmation = objectInputStream.readObject();
                String confirmation = (String) msgConfirmation;
                showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), confirmation);
                System.out.println(confirmation);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Une erreur s'est produite lors de l'envoie des données d'inscription");
            } catch (ClassNotFoundException ex) {
                System.out.println("Une erreur s'est produite lors de l'envoie des données d'inscription");
                throw new RuntimeException(ex);
            }

        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
