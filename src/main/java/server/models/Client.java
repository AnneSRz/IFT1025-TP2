package server.models;

import server.EventHandler;
import server.Server;
import server.ServerLauncher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private RegistrationForm inscription;
    private int port;
    private Socket socket;
    private Server server;


    public Client(int port, Server server) throws IOException {
        ArrayList<Course> coursListe = new ArrayList<>();
        this.port = port;
        //this.socket = new Socket("127.0.0.1",port);
        this.server = server;
    }

    // System.out.println("*** Bienvenue au portail de cours de l'UDEM ***");
    //System.out.println("Veuillez choisir la session pour laquelle vous voulez consultez la liste des cours:");

    // Step 1 Client récupère la liste des cours disponibles pour une session donnée, envoie une requete
    // charger au serveur
    private Course charger(String session) throws IOException {
        int i;
        char c;

        ArrayList<Course> coursSession = new ArrayList<>();
        this.server.handleLoadCourses(session);
        //ask the server to do handlerLoadCourses(session) via the socket
        //todo get array from socket
        InputStream inputStream = this.socket.getInputStream();

        // reads till the end of the stream
        while ((i = inputStream.read()) != -1) {

            // converts integer to character
            c = (char) i;

            // prints character
            System.out.print(c);
        }

        //return coursSession;


        // Envoie de la requete au serveur


        //  private void inscription () {
        // ArrayList<Course> coursFiltres = new ArrayList<>();
        // prendre les parametres dans inscription.txt de creer l'object and then faire le thingy

        return null;
    }
}