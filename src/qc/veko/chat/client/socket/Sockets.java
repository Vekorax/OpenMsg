package qc.veko.chat.client.socket;

import qc.veko.chat.client.Chat;
import qc.veko.chat.client.panels.Panel;
import qc.veko.easyswing.engine.EasyFrame;

import java.io.*;
import java.net.Socket;

public class Sockets extends Thread{
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private static Sockets instance;

    private boolean readingAwaitingMessage = false;

    public Sockets(Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) {
        instance = this;
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
    }


    @Override
    public void run() {
        while(true) {
            try {
                read();
            } catch(Exception e) {}
        }
    }

    //Reading messages that server is sending
    private void read() {
        String message = "You are connected! ";
        do {
            try {
                message = (String) in.readObject();
                System.out.println(message);
                if (message.equals("start")) {
                    // Panel for waiting in message loading
                }
                else if (message.equals("stop")) {
                    EasyFrame.getInstance().setPanel(new Panel());
                }
                Panel.getInstance().actualise(message);
            } catch (ClassNotFoundException classNotFoundException) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }while(!message.equals("close"));
    }

    public void dispose(){
        try{
            out.close();
            in.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //Sending message to the server so that it can be sent to another user
    public void send (String msg) {
        //Starting a thread to send a message
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //sending the message (using ObjectOutput to send other thing in the future)
                    out.writeObject(msg + "!" + Panel.getInstance().contactChosen);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sender.start();
    }
    //Sending your name for authentification (NEED TO BE CHANGE IN THE FUTURE)
    public void sendName (String msg) {
        //Starting the thread
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Sending the name of the user to the server to finish authentification of the user
                    out.writeObject("Name : " + msg);
                    out.flush();
                    Chat.getInstance().configAndSaveManager.addConfigLine("name", msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sender.start();
    }

    public static Sockets getInstance() {
        return instance;
    }
}
