package server;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import server.models.Course;
import server.models.RegistrationForm;
import java.util.Scanner;

/**
 * Le serveur peut éxécuter des commandes spécifiques tels que REGISTER_COMMAND et LOAD_COMMAND pour permettre a
 * un client de charger une liste de cours pour une session voulue et s'y inscrire.
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
     * Ce constructeur en prenant un paramètre type int, crèe ue instance qui se refère à la classe server sur un port,
     * ce port est donné et impose une limite de connexion qui est de 1 (file d'attente), Il définit aussi un
     * eventHandler pour gérer les évènements.
     *
     * @param port le numero du port pour les connexions au serveur
     * @throws IOException pour les erreurs qui sont produites lors le serverSocket est crée.
     *
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un EventHandler pour s'occuper des connexions
     * @param h l'evenemnt EventHandler qui gére les connexions
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Cette méthode avec la commande et l'argument associé à cette dernière communique avec les events handlers pour
     * qu'elle appelle la méthode qui correspond à la commande et l'argument en paramètre.
     *
     * @param cmd commande qui va être transmise au handlers qui reprendre une action qui va être éxécutée
     * @param arg argument qui va être utilisé avec la commande
     *
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Cette méthode initialise  et ecoute les connexions au serveur, Lorsqu'un client est connecté, il traite les
     * commandes qui sont envoyés. Il assure aussi la communication entre le serveur et le client.
     *
     * @throws IOException Au où cas des erreurs se produisent lors de la connexion ou de la transmission des données
     *
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                System.out.println(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * La méthode listen() écoute les informations et lit les lignes de texte qui lui sont transmises par le client via le
     * socket. Après avoir obtenu la commande et l'argument avec proccesCommandeLine();, elle appelle la méthode
     * alertHandlers en lui passant la commande et l'argument.
     *
     * @throws IOException pour les erreurs qui se prosuident lors de la lecture ou de l'écoute
     * @throws ClassNotFoundException lance une exception si la classe de l'object reçcu (par exmple Course) n'existe
     * pas ou n'est pas trouvée
     *
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     *
     * @param line la ligne de commande
     * @return
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * @throws IOException
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * @param cmd
     * @param arg
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     * La méthode filtre les cours par la session spécifiée en argument.
     * Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     * La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet
     * dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */

    public void handleLoadCourses(String arg) {
        try {
            ArrayList<Course> listeDeCours = new ArrayList<>();
            try {
                //Step 1 : read cours.txt file to get the information
                Scanner cours = new Scanner(new File("src/main/java/server/data/cours.txt"));

                while (cours.hasNext()) {
                    String line = cours.nextLine();
                    String[] lesCours = line.split("\t");
                    String nomCours = lesCours[0];
                    String sigle = lesCours[1];
                    String trimestre = lesCours[2];

                    listeDeCours.add(new Course(sigle, nomCours, trimestre));

                    //Test 1 : ArrayList de listes de Cours
                    //System.out.println(listeDeCours);
                }
            }catch (FileNotFoundException fe) {
                System.out.println("Le fichier n'a pas été trouvé");
            }

            //Step 3 : Filtrer les cours selon la session donnée en arguments dans la fonction
            ArrayList<Course> coursFiltres = new ArrayList<>();
            for (Course lesCours : listeDeCours) {
                if (lesCours.getSession().equals(arg)) {
                    coursFiltres.add(lesCours);
                }
            }
            //Test 2 : Pour une session donnée
            System.out.println(coursFiltres);

            //Step 4 : Retourner la liste d'objet Courses au client via le socket en utilisant ObjectOutputStream
            this.objectOutputStream.writeObject(coursFiltres);
            this.objectOutputStream.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erreur lors de l'écriture du fichier");
        }
    }

    /**
     * Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans
     * un fichier texte et renvoyer un message de confirmation au client.
     * La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier
     * ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {

            FileWriter fw = new FileWriter("src/main/java/server/data/inscription.txt", true);
            BufferedWriter writer = new BufferedWriter(fw);

            //Step 1 : Lire object registrationForm from Socket
            RegistrationForm donneesInscriptionRecues = (RegistrationForm) objectInputStream.readObject();

            // Step 2 : La mettre sous le format d'une liste pour pouvoir passer à travers
            ArrayList<RegistrationForm> donneesInscription = new ArrayList<>();
            donneesInscription.add(donneesInscriptionRecues);

            for (RegistrationForm formulaireInscription : donneesInscription){
                writer.append("\n" + formulaireInscription.getCourse().getSession() +"\t"+
                        formulaireInscription.getCourse().getCode() + "\t" + formulaireInscription.getMatricule() +
                        "\t" + formulaireInscription.getPrenom() + "\t" + formulaireInscription.getNom() + "\t" +
                        formulaireInscription.getEmail());
            }
            writer.close();

            //Step 3 : Envoie de la confirmation d'inscriptioin"
            for (int i = 0; i < donneesInscription.size(); i++ ){
                String confirmation = "Félicitations! Inscription réussie de " + donneesInscription.get(i).getPrenom() +
                        " au cours "  + donneesInscription.get(i).getCourse().getCode();

                this.objectOutputStream.writeObject(confirmation);
            }

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Erreur de l'écriture des données d'inscription");
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

