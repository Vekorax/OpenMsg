package qc.veko.chat.client;

import qc.veko.chat.client.manager.ConfigAndSaveManager;
import qc.veko.chat.client.panels.LoginPanel;
import qc.veko.easyswing.EasySwing;

import java.io.*;

public class Chat extends EasySwing {
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
        getFrame().setDefaultPanel(new LoginPanel()).setFrameResolution(1280, 720).setFrameTitle("Your Page Title");
        /*getFrame().setUndecorated(true);
        FrameDragListener frameDragListener = new FrameDragListener(getFrame());
        getFrame().addMouseListener(frameDragListener);
        getFrame().addMouseMotionListener(frameDragListener);*/
        launch();
    }

    public static Chat getInstance() {
        return instance;
    }
}
