package qc.veko.chat.server;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {

    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean running;
    private String name;

    //init all data for client
    public Connection(Socket client) {
        this.client = client;
        running = true;
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

    //Listening to all things client can be sending
    private void whileListening(){
        String message = "You are connected! ";
        do {
            try {
                //trying to read a chat message ( only chat message has been done for now)
                message = (String) input.readObject();
                //Initialise the authentification ( MUST BE CHANGE )
                if (message.startsWith("Name : ")) {
                    name = message.replace("Name : ", "");
                    //Save thread in map to localise the user thread
                    Server.id.put(name, this);
                    System.out.println(name + " is now Connected !");
                    //Asking for awaiting messages at login
                    askAwaitingMessage();
                } else {
                    //Spliting message to know who the message needs to be sent to
                    String msg[] = message.split("!");
                    String receiver = msg[1];
                    System.out.println(name + " : " + msg[0]);
                    //Send message to the right person
                    Server.send(msg[0], receiver, name);
                }
            }catch(ClassNotFoundException classNotFoundException){
                sendMessage("tf did you send? ");
            }catch (IOException e) {
                dispose();
            }
        }while(!message.equals("close") && running == true);
    }


    //When user disconect
    public void dispose(){
        try {
            //Close every sockets input, output, etc
            output.close();
            input.close();
            client.close();
            //Stopping the thread and killing it
            //Stopping it because they're could be some error in killing it
            running = false;
            Server.stopThread(name);
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
                Server.awaitingMessages.get(name).remove(msg);
            });
            //sendMessage("end");
        }
    }
}
