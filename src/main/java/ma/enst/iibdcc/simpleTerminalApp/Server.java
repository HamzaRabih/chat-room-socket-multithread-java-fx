package ma.enst.iibdcc.simpleTerminalApp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main (String[]Args) throws IOException {
        {
            ServerSocket serverSocket = new ServerSocket(9090);

            /**
             * établir un canal de communication pour lire et écrire des données entre un client et un serveur.
             */
            Socket client = serverSocket.accept();
            System.out.println("Connected to client");

            /**
             * Récupère le flux d’entrée de la socket client connecté
             * Ce flux sert à recevoir des données envoyées par l'autre extrémité (client ou serveur).
             * * Ici on veut lire du texte ligne par ligne à partir du flux d’entrée.
             * InputStreamReader convertit les octets en caractères (UTF-8 par défaut).
             * BufferedReader permet une lecture plus efficace et ligne par ligne via readLine().
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            /**
             * Récupère le flux de sortie de la socket client.
             * Ce flux sert à envoyer des données vers l'autre extrémité.
             * Cette ligne permet d’envoyer du texte facilement.
             * OutputStreamWriter convertit les caractères en octets.
             * PrintWriter fournit des méthodes comme println() pour envoyer des chaînes de caractères.
             * Le deuxième paramètre true signifie "auto-flush" :
             * chaque fois qu'on fait println(), les données sont envoyées immédiatement sans avoir à faire flush() manuellement.
             */
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

            Scanner sc = new Scanner(System.in);
            String msg;
            do {
                //Les messages Client
                msg = in.readLine();
                System.out.println("Client:" + msg);

                //Les messages envoyés au Client
                String sendMssg = sc.nextLine();
                out.println(sendMssg);

            } while (!msg.equals("bye"));

        }
    }
}

/*
public class Server {
    public static void main (String[]Args) throws IOException {
        {
            ServerSocket serverSocket = new ServerSocket(9090);
            Socket client = serverSocket.accept();
            System.out.println("Connected to client");
            InputStream is = client.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            OutputStream os = client.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os), true);
            Scanner sc = new Scanner(System.in);
            String msg;
            do {
                //Les messages Client
                msg = bf.readLine();
                System.out.println("Client:" + msg);
                String sendMssg = sc.nextLine();
                pw.println(sendMssg);

            } while (!msg.equals("bye"));

        }
    }
}
 */