import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

public class Client_simple {

    private Socket socket;
    private int port;
    public Client_simple(int port) throws IOException{
        //Step 1 : Connecter le client au serveur
        this.port = port;
    }
    public void charger() {
        try {
            this.socket = new Socket("127.0.0.1",this.port);

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

            //Step 3 : Envoie une requete charger au serveur "Charger"
            OutputStreamWriter os = new OutputStreamWriter(this.socket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(os);

            String line = "CHARGER";
            writer.append(line).append("\n");


            // Step 4 : Envoyer la session choisie au serveur qui sera utilisé comme arguments dans la fonction
            // handleLoadCourses
            writer.append(session);

            // Vider le buffer
            writer.flush();
            // writer.close();

            //Step 5 : Le client affiche ce qui est envoyé par le serveur aka la liste des cours triés
            System.out.println("les cours offerts pour la session d'" + session + " sont: ");

            InputStreamReader is = new InputStreamReader(this.socket.getInputStream());
            BufferedReader reader = new BufferedReader(is);

            // Tant que le serveur envoie des données on les affiche
            String lin ;
            while ((lin = reader.readLine()) != null) {
                System.out.println("Reçu : " + lin);
            }

            //System.out.println(cours);

        }catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erreur lors du Chargement");
        }
    }
}

// So askip le tpistes a dit qu'il faut 2 fonctions comme Server a handle load course et registration
/*
    private  RegistrationForm inscription() throws IOException {

        int optionVoulue = 0;
        String prenom;
        String nom;
        String email;
        String matricule;
        String cours;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //todo choisir une autre action
        while (optionVoulue < 1 || optionVoulue > 2) {
            System.out.print("> Choix : ");
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
            case 1:
                // Permettre a l'utilisateur de faire un autre choix
                charger();
                break;
            case 2:
                // Le client veut s'inscrire à un autre cours
                System.out.print("Veillez saisir votre prenom: " );
                prenom = br.readLine();
                System.out.print("Veillez saisir votre nom: " );
                nom = br.readLine();
                System.out.print("Veillez saisir votre email: " );
                email = br.readLine();
                System.out.print("Veillez saisir votre matricule: " );
                matricule = br.readLine();
                System.out.print("Veillez saisir le code du cours: " );
                cours = br.readLine();
                // Si le cours n'est pas dans la liste Step 7??
                for (Course coursInscris : charger().coursSession){
                    if (coursInscris.getName().equals(...){
                    }
                }
                break;

            default:
                break;
        }
        // Step 1 : Le client envoie une requête inscription au serveur

        // Step 2 : Le choix du cours doit être valide c.à.d le code du cours doit être présent dans la liste
        // des cours disponibles dans la session en question.

        // Step 3 : Envoyer l'objet donneeInscription au serveur
        RegistrationForm donneeInscription = new RegistrationForm(prenom, nom, email, matricule, Course cours);
        OutputStream os = server.getOutputStram();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        // Step 4 : Le serveur ajoute la ligne correspondante au fichier inscription.txt.
        oos.writeObject(donneeInscription);
        //public void handleRegistration() {}

        // Step 5 : Le client affiche ce message a la fin de l'inscription.
        System.out.println("Félicitations! Inscription réussie de " + prenom + "au cours" + cours);
        }
    }
}
*/
