package qc.veko.chat.client;

import qc.veko.chat.client.manager.ConfigAndSaveManager;
import qc.veko.chat.client.panels.LoginPanel;
import qc.veko.easyswing.EasySwing;

import java.io.*;

public class Chat {
    public ConfigAndSaveManager configAndSaveManager = new ConfigAndSaveManager();
    private static Chat instance;
    public static void main( String[] args ){
        new Chat();
    }
    public Chat() {
        instance = this;
        try {
            configAndSaveManager.loadMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Chat getInstance() {
        return instance;
    }
}
