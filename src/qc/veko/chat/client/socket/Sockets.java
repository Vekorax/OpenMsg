package qc.veko.chat.client.socket;

import qc.veko.chat.client.panels.Panel;

import java.io.*;
import java.net.Socket;

public class Sockets extends Thread{
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private static Sockets instance;

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

    private void read() {
        String message = "You are connected! ";
        do {
            try {
                message = (String) in.readObject();
                System.out.println(message);
                Panel.getInstance().actualise(message);
                //Panel.getInstance().addText(message);
                //ChatPanel.getInstance().message.add(message);
                //ChatPanel.getInstance().drawText();
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

    public void send (String msg) {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(msg + "!" + Panel.getInstance().contactChosen);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sender.start();
    }
    public void sendName (String msg) {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(msg);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sender.start();
    }

    public void askAwaitingMessage() {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject("Awaiting");
                    out.flush();
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
