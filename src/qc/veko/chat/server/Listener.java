package qc.veko.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread{

    private ServerSocket server;
    public boolean running;

    public Listener() {
        try {
            server = new ServerSocket(5000);
            System.out.println("Server Connected Waiting for Connection");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;
        try  {
            connect();
        } catch (IOException e) {
            run();
        }
    }

    private void connect() throws IOException{
        while(running){
            Socket client = server.accept();
            Runnable connection = new Connection(client);
            new Thread(connection).start();
        }
    }
}
