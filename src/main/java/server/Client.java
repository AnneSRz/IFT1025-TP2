package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private RegistrationForm inscription;
    private int port;
    private Server server;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Client(Server server){
       // ArrayList<Course> coursListe = new ArrayList<>();
        this.server = server;
    }

    // fonction private course changer marche pas????
    // private static void charger(String[] args) { ???
    private Course charger() {  // Fonctionnalite charger???? Une fonction charger?? AAAAAAAHHHHHHH
        //Step 1 : Se connecter au serveur genre Server.java...??

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
        ArrayList<Course> coursSession = new ArrayList<>();
        this.server.handleLoadCourses(session);
        //ask the server to do handlerLoadCourses(session) via the socket
        //todo get array from socket

        //Step 4 :  Le serveur doit récupérer la liste des cours du fichier cours.txt et l’envoie au client.

        // Step 5 : Le client récupère les cours et les affiche.
    }

    private  RegistrationForm inscription() {

        int optionVoulue = 0;

        //todo choisir une autre action
        System.out.println("> Choix : ");
        System.out.println("1. Consulter les cours offerts pour une autre session");
        System.out.println("2. Inscription à un cours");
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextInt()) {
            optionVoulue = scanner.nextInt();
                if (optionVoulue < 1 || optionVoulue > 2){
                    System.out.println("Option invalide");
                    System.out.println("Faites un autre choix");
                }
            }
        else {
            System.out.println("Option invalide");
            System.out.println("Faites un autre choix entre 1 et 2");
            scanner.next();
        }
        switch (optionVoulue) {
            case 1:
                // Permettre a l'utilisateur de faire un autre choix
                charger();
                break;
            case 2:
                // Le client veut s'inscrire à un autre cours
                break;

            default:
                break;
        }
        // Step 6 : Le client envoie une requête inscription au serveur
        // Step 7 : Le choix du cours doit être valide c.à.d le code du cours doit être présent dans la liste
        // des cours disponibles dans la session en question.
        // Le serveur ajoute la ligne correspondante au fichier inscription.txt et envoie un message de réussite au client.
        // Le client affiche ce message (ou celui de l’échec en cas d’exception).


    }
}


