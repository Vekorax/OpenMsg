package qc.veko.chat.client.utils;

import qc.veko.chat.client.socket.Sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Utils {
    public static void connectSocketForLogin(String name) {
        //Getting a connection whit the server
        final Socket clientSocket;
        final ObjectOutputStream out;
        final ObjectInputStream in;
        try { // 45.140.165.62
            clientSocket = new Socket("127.0.0.1", 5000);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            Runnable connection = new Sockets(clientSocket, in, out);
            new Thread(connection).start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //Sending The name of the user so the server can identify the user
        Sockets.getInstance().sendName(name);
    }
}
