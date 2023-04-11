import server.models.Course;
import server.models.RegistrationForm;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client_simple {

    private Socket socket;
    private int port;
    public Client_simple(int port) throws IOException{
        //Step 1 : Connecter le client au serveur
        this.port = port;
    }
    //TODO
    // Pour appeler Server Launcher a partir du client, il faut soit :
    // 1. Appeler le fichier server.jar à partir du client en utilisant la ligne de commande, ou
    // 2. Appeler le ServerLauncher et Client en utilisant le multithreading.
    // ????
    public void charger() {
        try {
            this.socket = new Socket("127.0.0.1",this.port);
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
            String session = "";
            for (String varTempSession : sessionsListes) {
                session = varTempSession;
            }
            System.out.println("les cours offerts pour la session d'" + session + " sont: ");

            //Step 3 : Envoie une requete charger au serveur "Charger avec la session choisie"
            String requete = "CHARGER " + session;
            objectOutputStream.writeObject(requete);

            //TODO ******************************
            //Step 4 : Le client recupere la liste des cours envoyes par le serveur"
            Course mesCours = (Course) objectInputStream.readObject();

            //Step 5 : Le client affiche ce qui est envoyé par le serveur soit la liste des cours triés
            System.out.println(mesCours);

            // Step 6 : Fermer les Streams
            objectOutputStream.close();
            objectInputStream.close();
            // Step 7 : Proceder a la partie de l'inscription
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
        String cours = "";
        try{
            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(socket.getOutputStream());
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
                    // Step 3 : Le client veut s'inscrire à un autre cours
                    System.out.print("Veillez saisir votre prenom: ");
                    prenom = br.readLine();
                    System.out.print("Veillez saisir votre nom: ");
                    nom = br.readLine();
                    System.out.print("Veillez saisir votre email: ");
                    email = br.readLine();
                    System.out.print("Veillez saisir votre matricule: ");
                    matricule = br.readLine();
                    System.out.print("Veillez saisir le code du cours: ");
                    cours = br.readLine().toUpperCase();
                    System.out.println(cours);

                    //TODO
                    // Step 4 : Valider le cours choisi - code du cours (le cours ou le client s'inscris) doit être
                    // présent dans la liste des cours disponibles pour la session en question
                }
                default -> {
                }

            }
            // Step 5 : Envoyer une requete Inscription au server
            String requeteInscription = "Inscription " ;
            objectOutputStream2.writeObject(requeteInscription);
            objectOutputStream2.flush(); // Envoyer la requete tout de suite

            //TODO
            // Step 6 : Envoyer l'objet Registrationform au serveur
            //RegistrationForm donneesInscription = new RegistrationForm(prenom, nom, email, matricule, Course cours);
            //objectOutputStream2.writeObject(donneesInscription);

            // Step 7 : Le client affiche ce message a la fin de l'inscription.
            System.out.println("Félicitations! Inscription réussie de " + prenom + " au cours " + cours);
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur au courant de l'inscription");
        }
    }
}



