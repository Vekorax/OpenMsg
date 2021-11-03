package qc.veko.chat.client.panels;

import qc.veko.chat.client.socket.Sockets;
import qc.veko.chat.client.utils.Utils;
import qc.veko.easyswing.engine.EasyFrame;
import qc.veko.easyswing.engine.EasyPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginPanel extends EasyPanel {
    public LoginPanel () {
        setLayout(null);
        setBackGroundImage("/qc/veko/chat/client/others/chat software background.png");

        //Setting a label to tell the user what to write
        JLabel text = new JLabel("Enter Name : ");
        text.setBounds(50, 500, 1100, 50);
        text.setFont(text.getFont().deriveFont(45f));
        text.setForeground(Color.white);
        add(text);

        //Adding JTextField so user can identify himself
        JTextField textField = new JTextField();
        textField.setFont(textField.getFont().deriveFont(20f));
        textField.setBounds(380, 628, 868, 70);
        textField.setOpaque(false);
        textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        textField.setForeground(Color.white);
        add(textField);
        //Adding a listener for when the user has pressed enter
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    Utils.connectSocketForLogin(textField.getText());
                    //Setting the main panel as the chatting panel
                    EasyFrame.getInstance().setPanel(new Panel());
                }
            }
        });

    }
}
