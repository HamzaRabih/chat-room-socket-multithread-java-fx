package ma.enst.iibdcc.chatServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServerMultithread {

    // Liste partagée de tous les flux de sortie clients connectés
    private static Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>());


    public static void main(String[] args) throws IOException {
        // Création du serveur sur le port 9090
        ServerSocket ss = new ServerSocket(9090);
        System.out.println("Serveur en écoute sur le port 9090...");

        // Boucle infinie pour accepter plusieurs clients
        while (true) {
            Socket s = ss.accept(); // Attente de connexion d’un client
            System.out.println("Client connecté : " + s.getInetAddress());

            // Création et démarrage d’un thread pour gérer le client
            SocketThread socketThread = new SocketThread(s,clientWriters);
            socketThread.start();
        }

    }
}
