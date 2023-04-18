import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Le client va envoyer des reqêtes au serveur pour permettre a un utilisateur de visionner des cours pour une session
 * donnée et de s'inscrire au cours de son choix.
 */
public class Client_simple {
    private int port;
    private ArrayList<Course> coursFiltres;
    public Client_simple(int port) throws IOException{
        this.port = port;
    }

    /**
     * Cette méthode envoie une requête Charger avec une session choisie par le client au serveur, va chercher la liste
     * des cours qui lui ont été envoyés et l'affiche.
     *
     * @throws ClassNotFoundException Au cas où l'object Course de la liste des cours envoyés par le serveur
     *  n'a pas pu être lu.
     * @throws IOException Lance une exception si une erreur se produit lors de la connexion entre le serveur et le
     *  client
     */
    public void charger() {
        try {
            //Step 1 : Connecter le client au serveur
            Socket socket = new Socket("127.0.0.1",this.port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Step 2 : Le client doit choisir la session pour laquelle il veut voir les cours
            int sessionChoisie = 0;
            String[] sessionsListes = {"Automne", "Hiver", "Ete"};

            System.out.println("*** Bienvenue au portail de cours de l'UDEM ***");
            System.out.println("Veuillez choisir la session pour laquelle vous voulez consultez la liste des cours:");

            while (sessionChoisie < 1 || sessionChoisie > 3) {
                for (int i = 0 ; i < sessionsListes.length; i++)
                    System.out.println((i+1) + "." + sessionsListes[i]);

                System.out.print("> Choix : ");

                // Step 3 : Valider le choix du client pour la session qu'il veut consulter
                Scanner scan = new Scanner(System.in);
                if (scan.hasNextInt()) {
                    sessionChoisie = scan.nextInt();
                    // Step 3.1 : Est-ce que l'entier est bien entre 1 et 3 inclusivement
                    if (sessionChoisie < 1 || sessionChoisie > 3 ) {
                        System.out.println("Choix de cours invalide");
                        System.out.println("Faites un autre choix");
                    }
                } else {
                    // Step 3.2 :  Afficher ce message si le client écrit sur la ligne de commande un caractere autre
                    // qu'un entier par exemple
                    System.out.println("Choix de cours invalide");
                    System.out.println("Faites un autre choix entre 1 et 3");
                    scan.next();
                }
            }
            String session = sessionsListes[sessionChoisie-1];

            System.out.println("les cours offerts pour la session d'" + session + " sont: ");

            //Step 4 : Envoie une requete charger au serveur "Charger avec la session choisie"
            String requete = "CHARGER " + session;
            objectOutputStream.writeObject(requete);
            objectOutputStream.flush();

            //Step 5 : Le client recupere la liste des cours envoyes par le serveur"
            this.coursFiltres = (ArrayList<Course>) objectInputStream.readObject() ;

            //Step 6 : Le client affiche ce qui est envoyé par le serveur soit la liste des cours triés
            for (int i = 0; i < coursFiltres.size(); i++ ){
                System.out.println((i+1)+". " + coursFiltres.get(i).getCode() + " " + coursFiltres.get(i).getName());
            }
            // Step 7 : Fermer les flux d'entrées et de sorties pour éviter les problèmes
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();

            // Step 8 : Proceder a la partie de l'inscription
            inscription();

        }catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erreur lors du Chargement la liste de cours n'a pas pu être affiché");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode se connecte au serveur et lui envoie une requête Inscrire avec les donnnées du formulaire
     * d'inscription. Elle permet aussi a l'utilisateur de renvoyés une autre requête charger s'il veut voir les cours
     * pour une autre session. De plus, elle s'assure que le cours dans lequel l'utilisateur veut s'inscrire est
     * disponible dans la session qui à été choisie. Pour finir inscription() affiche le message de confirmation
     * envoyés par le serveur lorsque l'inscription est complétée.
     *
     * @throws IOException Lance une exception si une erreur se produit lors de l'envoie de la reqête et des données
     * d'inscription.
     * @throws IOException Lance une autre exception si une erreur se produit lors de la connexion entre le serveur
     * et le client
     */
    public void inscription() {

        int optionVoulue = 0;
        String prenom = "";
        String nom = "";
        String email = "";
        String matricule = "";
        String code = "";
        RegistrationForm coursInscrit = null;
        try{
            // Step 1 : Se reconnecter au serveur
            Socket socket = new Socket("127.0.0.1",this.port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Step 2 : Permettre au client de changer de cours ou de choisir de s'inscrire s'il est satisfait des choix
            // dispos pour la session qui est affichée
            while (optionVoulue < 1 || optionVoulue > 2) {
                System.out.println("> Choix : ");
                System.out.println("1. Consulter les cours offerts pour une autre session");
                System.out.println("2. Inscription à un cours");

                System.out.print("> Choix : ");
                Scanner scanner = new Scanner(System.in);

                // Step 2.1 : Tant que le client n'a pas fait un choix valide entre 1 et 2 lui proposer les options
                // précédentes

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
                case 1 -> {
                    // Step 2 : Permettre a l'utilisateur de chosir une autre session
                    socket.close();
                    objectOutputStream.close();
                    objectInputStream.close();
                    charger();
                }
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
                    boolean codeValide = false;
                    for (Course element : coursFiltres) {
                        if (element.getCode().compareTo(code) == 0) {
                            coursCours = element;
                            codeValide = true;
                            break;
                        }
                    }
                    if(!codeValide) {
                        System.out.println("Vous devez choisir un cours qui se trouve dans la liste affichée " +
                                "précédemment pour la session choisie. Ex: IFT1025");
                        socket.close();
                        objectOutputStream.close();
                        objectInputStream.close();
                        inscription();
                    }
                    try {

                        coursInscrit = new RegistrationForm(prenom, nom, email, matricule, coursCours);
                        // Step 5 : Envoyer une requete Inscription au server
                        String requeteInscription = "INSCRIRE";
                        objectOutputStream.writeObject(requeteInscription);
                        objectOutputStream.flush(); // jusqu'a la ça marche la requete est envoyé

                        //Step 6 : Envoyer l'objet Registrationform au serveur
                        objectOutputStream.writeObject(coursInscrit);
                        objectOutputStream.flush();

                        // Step 7 : Lire le message de confirmation du serveur.
                        Object msgConfirmation = objectInputStream.readObject();
                        String confirmation = (String) msgConfirmation;
                        System.out.println(confirmation);

                    }catch (IOException ex){
                        ex.printStackTrace();
                        System.out.println("Une erreur s'est produite lors de l'envoie des données d'inscription");
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            socket.close();
            objectOutputStream.close();
            objectInputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur au courant de l'inscription");
        }
    }
}