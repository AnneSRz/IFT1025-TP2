import server.models.Course;
import server.models.RegistrationForm;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Client_simple {

    private Socket socket;
    private int port;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ArrayList<Course> coursFiltres;
    public Client_simple(int port) throws IOException{
        //Step 1 : Connecter le client au serveur
        this.port = port;
        this.socket = new Socket("127.0.0.1",this.port);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    }
    //TODO
    // Pour appeler Server Launcher a partir du client, il faut soit :
    // 1. Appeler le fichier server.jar à partir du client en utilisant la ligne de commande, ou
    // 2. Appeler le ServerLauncher et Client en utilisant le multithreading.
    // ????
    public void charger() {
        try {

            // Step 2 : Le client doit choisir la session pour laquelle il veut voir les cours
            int sessionChoisie = 0;
            String[] sessionsListes = {"Automne", "Hiver", "Ete"};

            System.out.println("*** Bienvenue au portail de cours de l'UDEM ***");
            System.out.println("Veuillez choisir la session pour laquelle vous voulez consultez la liste des cours:");

            while (sessionChoisie < 1 || sessionChoisie > 3) {
                for (int i = 0 ; i < sessionsListes.length; i++)
                    System.out.println((i+1) + "." + sessionsListes[i]);

                System.out.print("> Choix : ");

                Scanner scan = new Scanner(System.in);
                if (scan.hasNextInt()) {
                    sessionChoisie = scan.nextInt();
                    if (sessionChoisie < 1 || sessionChoisie > 3 ) {
                        System.out.println("Choix de cours invalide");
                        System.out.println("Faites un autre choix");
                    }
                } else {
                    System.out.println("Choix de cours invalide");
                    System.out.println("Faites un autre choix entre 1 et 3");
                    scan.next();
                }
            }
            String session = sessionsListes[sessionChoisie-1];

            System.out.println("les cours offerts pour la session d'" + session + " sont: ");

            //Step 3 : Envoie une requete charger au serveur "Charger avec la session choisie"
            String requete = "CHARGER " + session;
            objectOutputStream.writeObject(requete);
            objectOutputStream.flush();

            //Step 4 : Le client recupere la liste des cours envoyes par le serveur"
            this.coursFiltres = (ArrayList<Course>) objectInputStream.readObject() ;

            //Step 5 : Le client affiche ce qui est envoyé par le serveur soit la liste des cours triés
            for (int i = 0; i < coursFiltres.size(); i++ ){
                System.out.println((i+1)+". " + coursFiltres.get(i).getCode() + " " + coursFiltres.get(i).getName());
            }

            // Step 6 : Proceder a la partie de l'inscription
            inscription();

        }catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erreur lors du Chargement la liste de cours n'a pas pu être affiché");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void inscription() {

        int optionVoulue = 0;
        String prenom = "";
        String nom = "";
        String email = "";
        String matricule = "";
        String code = "";
        RegistrationForm coursInscrit = null;
        try{
            //objectOutputStream2 = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Step 1 : Changer de cours ou Inscription
            while (optionVoulue < 1 || optionVoulue > 2) {
                System.out.println("> Choix : ");
                System.out.println("1. Consulter les cours offerts pour une autre session");
                System.out.println("2. Inscription à un cours");

                System.out.print("> Choix : ");
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNextInt()) {
                    optionVoulue = scanner.nextInt();
                    if (optionVoulue < 1 || optionVoulue > 2) {
                        System.out.println("Option invalide");
                        System.out.println("Faites un autre choix");
                    }
                } else {
                    System.out.println("Option invalide");
                    System.out.println("Faites un autre choix entre 1 et 2");
                    scanner.next();
                }
            }
            switch (optionVoulue) {
                case 1 ->
                    // Step 2 : Permettre a l'utilisateur de faire un autre choix
                        charger();
                case 2 -> {
                    // Step 3 : Le client veut s'inscrire à un cours
                    System.out.print("Veuillez saisir votre prenom: ");
                    prenom = br.readLine();
                    System.out.print("Veuillez saisir votre nom: ");
                    nom = br.readLine();
                    System.out.print("Veuillez saisir votre email: ");
                    email = br.readLine();
                    System.out.print("Veuillez saisir votre matricule: ");
                    matricule = br.readLine();
                    System.out.print("Veuillez saisir le code du cours: ");
                    code = br.readLine().toUpperCase();

                    // Step 4 : Valider le cours choisi - code du cours (le cours ou le client s'inscris) doit être
                    // présent dans la liste des cours disponibles pour la session en question
                    Course coursCours = null;
                    for (Course element : coursFiltres){
                        if (element.getCode().compareTo(code) == 0){
                            coursCours = element;
                            break;
                        }else{
                            System.out.println("Vous devez choisir un cours qui se trouve dans la liste affichée " +
                                    "précédemment pour la session choisie. Ex: IFT1025" );
                            inscription();
                        }
                    }
                    coursInscrit = new RegistrationForm(prenom,nom,email,matricule,coursCours);
                }

            }
            // Step 5 : Envoyer une requete Inscription au server
            String requeteInscription = "INSCRIRE" ;
            objectOutputStream.writeObject(requeteInscription);

            objectOutputStream.writeObject(requeteInscription);
            objectOutputStream.flush();

            //Step 6 : Envoyer l'objet Registrationform au serveur
            objectOutputStream.writeObject(coursInscrit);
            objectOutputStream.flush();
            //objectOutputStream.flush(); // Envoyer la requete tout de suite

            // Step 7 : Lire le message de confirmation du serveur.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(objectInputStream));
            String msgConfirmation = bufferedReader.readLine();
            System.out.println(msgConfirmation);
            objectOutputStream.close();


        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur au courant de l'inscription");
        }
    }
}
