import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // private static void charger(String[] args) { ???
        // Fonctionnalite charger???? Une fonction charger?? AAAAAAAHHHHHHH
        //Step 1 : Connecter le client au serveur genre ??

        // Step 2 : Client récupère la liste des cours disponibles pour une session donnée
        int sessionChoisie = 0;
        String session = "";
        String[] sessionsListes = {"Automne", "Hiver", "Ete"};

        System.out.println("*** Bienvenue au portail de cours de l'UDEM ***");
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consultez la liste des cours:");

        while (sessionChoisie < 1 || sessionChoisie > 3) {
            System.out.println("1. " + sessionsListes[0]);
            System.out.println("2. " + sessionsListes[1]);
            System.out.println("3. " + sessionsListes[2]);

            System.out.print("> Choix : ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                sessionChoisie = scan.nextInt();
                if (sessionChoisie < 1 || sessionChoisie > 3) {
                    System.out.println("Choix de cours invalide");
                    System.out.println("Faites un autre choix");
                }
            } else {
                System.out.println("Choix de cours invalide");
                System.out.println("Faites un autre choix entre 1 et 3");
                //scan.next();
            }
        }

        switch (sessionChoisie) {
            case 1:
                session = sessionsListes[0];
                break;
            case 2:
                session = sessionsListes[1];
                break;

            case 3:
                session = sessionsListes[2];
                break;

            default:
                break;

        }

        System.out.println("les cours offerts pour la session d' " + session + " sont: ");
        // Step 3 : Envoie une requete charger au serveur (handleLoadCourses)

        //ask the server to do handlerLoadCourses(session) via the socket
        //todo get array from socket

        //Step 4 :  Le serveur doit récupérer la liste des cours du fichier cours.txt et l’envoie au client.

        // Step 5 : Le client récupère les cours et les affiche.

    }
}

// So askip le tpistes a dit qu'il faut 2 fonctions comme server a handle load course et registration
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
