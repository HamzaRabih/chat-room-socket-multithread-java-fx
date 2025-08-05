package ma.enst.iibdcc.simpleTerminalApp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] Args) throws IOException {

        Socket socket = new Socket("localhost", 9090);
        System.out.println("Connected to server");

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        Scanner sc = new Scanner(System.in);
        String msg;
        do {
            String sendMess = sc.nextLine();
            pw.println(sendMess);
            msg = br.readLine();
            System.out.println("server:" + msg);
        } while (!msg.equals("bye"));


    }
}
