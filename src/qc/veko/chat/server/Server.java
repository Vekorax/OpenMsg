package qc.veko.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private static Server instance;

    public static Map<String, Connection> id = new HashMap<>();
    public static Map<String, List<String>> awaitingMessages = new HashMap<>();

    public Server() {
        instance = this;
    }

    public static void main( String[] args ) {
        Runnable connection = new Listener();
        new Thread(connection).start();
        new Server();
    }

    public static void send (String msg, String name, String sender) throws IOException {
        if(!id.containsKey(name)) {
            if(awaitingMessages.containsKey(name)) {
                awaitingMessages.get(name).add(sender + " : " + msg);
            } else {
                List<String> newAwaitingMessageList = new ArrayList<>();
                newAwaitingMessageList.add(sender + " : " + msg);
                awaitingMessages.put(name, newAwaitingMessageList);
            }
            return;
        }
        Connection connection = id.get(name);
        connection.sendMessage(msg, sender);
    }

    public static void stopThread(String name) {
        Thread connection = id.get(name);
        connection.interrupt();
        id.remove(name);
    }

    public void askConnectionState() {

    }

    public static Server getInstance() {
        return instance;
    }
}
