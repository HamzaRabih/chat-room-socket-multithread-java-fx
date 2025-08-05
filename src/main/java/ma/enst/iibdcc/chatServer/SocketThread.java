package ma.enst.iibdcc.chatServer;

import java.io.*;
import java.net.Socket;
import java.util.Set;

// Classe qui gère la communication avec un client (Thread)
public class SocketThread extends Thread {

    private Socket socket;
    private Set<PrintWriter> clientWriters;
    private PrintWriter out;

    // Constructeur qui reçoit la socket client
    public SocketThread(Socket socket, Set<PrintWriter> clientWriters) {
        this.socket = socket;
        this.clientWriters = clientWriters;
    }

    @Override
    public void run() {
        try {
            // Pour lire les messages du client
            //lit les messages envoyés par le client.
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            clientWriters.add(out);

            // Message de bienvenue
            out.println("Bienvenue sur le serveur de chat !");
            String message;

            // Boucle d’échange de messages
            while ((message = in.readLine()) != null) {
                System.out.println("Message reçu : " + message);

                // Broadcast du message à tous les clients
                synchronized (clientWriters) {
                    for (PrintWriter writer : clientWriters) {
                        writer.println(message);
                    }
                }
                if (message.equalsIgnoreCase("bye")) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                clientWriters.remove(out);
            }
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
