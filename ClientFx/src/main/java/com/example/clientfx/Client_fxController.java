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
/**
 *
 */
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
    private TableView<Course> coursesTable;
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
    //</editor-fold>

    /**
     * Cette méthode initialise le controleur, se connecte au serveur et verifie si une session a été choisie dans la
     * liste déroulante. Si une session est choisie,il fait appel à la fonction handle Charge
     *
     * @param url le paramètre url permet d'organiser le contenu et de trouver les informations dans le fichiers FXML
     * @param resourceBundle Pour les objects locaux, si le programme a besoin d'une ressource, ressouceBundle peut le
     * charger
     * @throws IOException Lance une exception si une erreur se produit lors de la connection au serveur
     */
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

    /**
     * Cette méthode envoie une requête Charger avec une session choisie par le client au serveur, va chercher la liste
     * des cours qui lui ont été envoyés et l'affiche dans un tableau. Elle permet aussi a l'utilisateur de choisir
     * plusiueurs fois une session différente pour voir les cours disponibles
     *
     * @throws ClassNotFoundException Au cas où l'object Course de la liste des cours envoyés par le serveur
     * n'a pas pu être lu.
     * @throws IOException Lance une exception si une erreur se produit lors de la connexion entre le serveur et le
     * client
     */
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

                    // Step 5 : Affiché la liste des cours dans le tableau
                    coursesTable.getItems().clear();

                    ObservableList<Course> listeDesCours = FXCollections.observableList(this.coursFiltres);
                    this.coursesTable.setItems(listeDesCours);
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

    /**
     * Cette méthode permet de valider si le format de l'email que l'utilisateur a entrée dans la boîte de texte est
     * conforme.
     *
     * @param text1 Prend en paramètre le texte que l'utilisateur a entré qui correspond à l'email qui doit être validé.
     * @return true - l'adresse email est valide. Sinon false
     */
    // Valider le Email qui respecte le bon format
    private static boolean emailValidation(String text1) {
        return text1.matches("^[\\w!#$%&'*+/=?`{|}~^.-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^.-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    /**
     * @param text2 Prend en paramètre le texte que l'utilisateur a entré qui correspond au matricule qui doit être validé.
     * @return true -  l'adresse email est valide. Sinon false
     */
    // Valider le matricule que le matricule respecte le bon format
    private static boolean matriculeValidation(String text2) {
        return text2.matches("^[0-9]{8}$");
    }

    /**
     * Cette méthode permet de vérifier s'il y a des erreurs dans le formulaire d'inscription lorsque l'utilisateur
     * appuie sur le bouton envoyé. De ce fait, toutes les conditions doivent être remplies pour pouvoir envoyer une
     * demande d'inscription (les champs ne doivent pas être vides, l'email et le matricule doivent être valide et un
     * un cours doit être sélectionné)
     *
     * @param actionEvent l'évènement du clic de la souris sur le bouton envoyé
     */
    @FXML
    public void handle(ActionEvent actionEvent) {
        // Step 1 : Un cours doit être sélectionné dans le tableau
        this.leCours =  this.coursesTable.getSelectionModel().getSelectedItems();
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

    /**
     * Cette méthode affiche une boîte de dialogue relatifs aux erreurs dans le formulaire d'inscription lorsque
     * l'utlisateur appuie sur le bouton envoyé
     *
     * @param error le type d'erreur (Alerte ou confirmation)
     * @param window la fenêtre pour l'alerte
     * @param Message le message qui va être affiché dans la boîte de dialogue
     */
    private void showAlert(Alert.AlertType error, Window window, String Message) {
        Alert messageAlert = new Alert(error);
        String messageErreur = "Message";
        messageAlert.setTitle(messageErreur);
        messageAlert.setContentText(Message);
        messageAlert.initOwner(window);
        messageAlert.show();
    }


    /**
     * Cette méthode se connecte au serveur et lui envoie une requête Inscrire avec les donnnées du formulaire
     * d'inscription.
     *
     * @throws IOException Lance une exception si une erreur se produit lors de l'envoie de la reqête et des données
     * d'inscription.
     * @throws UnknownHostException Lance une exception si une erreur se produit lors de la connexion entre le serveur
     * et le client
     */
    public void handleRegistration() {
        RegistrationForm donneesInscription = null;
        try {
            // Step 1 : Se reconnecter au serveur
            Socket socket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Step 2 : Obtenir les données relatifs au formulaire d'inscription entrées par l'utilisateur

            String prenom = prenomField.getText();
            String nom = nomField.getText();
            String email = emailField.getText();
            String matricule = matriculeField.getText();


            // Step 3 : Extraire les informations du cours sélectionné du tableaux
            Course coursSelectionne = coursesTable.getSelectionModel().getSelectedItem();

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
                showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(), confirmation);
                System.out.println(confirmation);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Une erreur s'est produite lors de l'envoie des données d'inscription");
            } catch (ClassNotFoundException ex) {
                System.out.println("Une erreur s'est produite lors de l'envoie des données d'inscription");
                throw new RuntimeException(ex);
            }

        } catch (UnknownHostException ex) {
            System.out.println("Une erreur s'est produite lors de la connexion au serveur");
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
