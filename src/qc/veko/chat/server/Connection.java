package qc.veko.chat.server;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {

    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean running;
    private String name;

    public Connection(Socket client) {
        this.client = client;
        //running=true;
        try {
            setupStreams();
            whileListening();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not connect to: "+ client.getInetAddress().getHostName());
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                whileListening();
            } catch(Exception e) {}
        }
    }

    private void whileListening(){
        String message = "You are connected! ";
        do {
            try {
                message = (String) input.readObject();
                if (message.startsWith("Name : ")) {
                    name = message.replace("Name : ", "");
                    Server.id.put(name, this);
                    System.out.println(name + " is now Connected !");
                    askAwaitingMessage();
                } else {
                    String msg[] = message.split("!");
                    String receiver = msg[1];
                    System.out.println(name + " : " + msg[0]);
                    Server.send(msg[0], receiver, name);
                }
            }catch(ClassNotFoundException classNotFoundException){
                sendMessage("tf did you send? ");
            }catch (IOException e) {
                dispose();
            }
        }while(!message.equals("close") && running == true);
    }


    public void dispose(){
        try {
            output.close();
            input.close();
            client.close();
            interrupt();
            running = false;
            Server.stopThread(name);
            //Server.stopThread(name);
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public  void sendMessage(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void sendMessage(String message, String sender) {
        try {
            output.writeObject(sender + " : " + message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }

    private void askAwaitingMessage() {
        if (Server.awaitingMessages.containsKey(name)) {
            //sendMessage("start");
            Server.awaitingMessages.get(name).forEach(msg -> {
                sendMessage(msg);
            });
            Server.awaitingMessages.remove(name);
            //sendMessage("end");
        }
    }
}
