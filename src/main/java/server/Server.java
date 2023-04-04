package server;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import server.models.Course;
import java.io.File;
import server.models.RegistrationForm;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Le serveur peut éxécuter des commandes spécifiques tels que REGISTER_COMMAND et LOAD_COMMAND pour permettre a
 * un client charger une liste de cours pour une session voulue et s'y inscrire.
 * Cette classe permet de créer un serveur et ensuite de lire et écrire des objets entre le serveur et le client,
 * tout en gérant les commandes qui sont envoyées par le client.
 */
public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Ce constructeur en prenant un paramètre type int, crèe ue instance se refère à la classe server sur un port,
     * ce port est donné et la limite d'attente de connexion est de 1, ça définit un eventHandler pour gérer les
     * évènements sur le serveur
     *
     * @param port le numero du port pour les connexions au serveur
     * @throws IOException Pour les erreurs qui sont produites lors le serverSocket est cree.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un EventHandler pour s'occuper des connexions
     * @param h l'evenemnt EventHandler qui est ajouter pour s'coccuper des connexions
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * C'est une metohode ça??
     * @param cmd
     * @param arg
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    private ArrayList<Course> listeDeCours = new ArrayList<>();

    public void handleLoadCourses(String arg) {
        try{
            // ???? pt pas necessaire non plus puisqu'en executant serverlauncher ça istancier server et l'executer sur le port 1337:
            ServerSocket ss = new ServerSocket(1337);
            Socket socket = ss.accept();

            //Step 1 : read cours.txt file to get the information
            File fichierTexte = new File("/src/main/Java/server/data/cours.txt");
            Scanner cours = new Scanner(fichierTexte);
            cours.useDelimiter("\t");

            while (cours.hasNext()){
                String nomCours = cours.next();
                String sigle = cours.next();
                String trimestre = cours.next();

                Course coursDisponibles = new Course(nomCours, sigle, trimestre);
                this.listeDeCours.add(coursDisponibles);
                System.out.println(listeDeCours);
            }

            //Step 2 : Filtrer les cours selon la session donnée en arguments dans la fonction
            ArrayList<Course> coursFiltres = new ArrayList<>();
            for (Course lesCours : listeDeCours){
                if (lesCours.getSession().equals(arg)){
                    coursFiltres.add(lesCours);
                }
            }
            System.out.println(coursFiltres);

            //Step 3 : Retourner la liste d'objet Courses au client via le socket en utilisant ObjectOutputStream
            //pt pas necessaire puisqu'en executant serverauncher ça istancier server et l'executer sur le port 1337:
            // ServerSocket ss = new ServerSocket(1337);
            // Socket client = ss.accept();
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(coursFiltres);

            client.close();
            output.close();

        }catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erreur lors de la lecture du fichier");
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {
            //Step 1 : Lire object registrationForm from Socket

            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            RegistrationForm inscription = (RegistrationForm)input.readObject();
            System.out.println(inscription); // A enlever

            //Step 2 : Save the object in Inscription.txt file (attention au format)
            FileWriter fw = new FileWriter("inscription.txt");
            BufferedWriter writer = new BufferedWriter(fw);

            //Step 2.1 : Write object in inscription.txt
            writer.write(inscription.getCourse().getSession() + "\t");
            writer.write(inscription.getCourse().getCode() + "\t");
            writer.write(inscription.getMatricule() + "\t");
            writer.write(inscription.getPrenom()+ "\t");
            writer.write(inscription.getNom() + "\t");
            writer.write(inscription.getEmail() + "\t");

            input.close();
            fw.close();

        }catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
            System.out.println("Erreur lors de l'inscription");
        }
    }
}

