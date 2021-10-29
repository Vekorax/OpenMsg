package qc.veko.chat.client.panels;

import qc.veko.chat.client.socket.Sockets;
import qc.veko.easyswing.engine.EasyPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class ChatPanel extends EasyPanel {

    public List<String> message = new LinkedList<String>();
    public List<String> contact = new LinkedList<String>();
    private String name;
    private static ChatPanel instance;
    JTextField sendTo = new JTextField();
    public String receiver = sendTo.getText();

    public ChatPanel (String name) {
        instance = this;
        this.name = name;
        setLayout(new GridBagLayout());
        //setLayout(null);
        setBackGroundImage("/qc/veko/chat/client/others/chat software background.png");
        //setBackgroundColor(Color.BLACK);
        init();
    }

    private void init() {

        JLabel text = new JLabel(name);
        text.setBounds(50, 25, 1100, 50);
        text.setFont(text.getFont().deriveFont(45f));
        text.setForeground(Color.white);
        add(text);

        sendTo.setFont(sendTo.getFont().deriveFont(20f));
        sendTo.setBounds(900, 550, 250, 50);
        add(sendTo);
        sendTo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    receiver = sendTo.getText();
                }
            }
        });

        JTextField textField = new JTextField();
        textField.setFont(textField.getFont().deriveFont(20f));
        textField.setBounds(50, 600, 1100, 50);
        add(textField);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    Sockets.getInstance().send(textField.getText());
                    message.add("You : " + textField.getText());
                    textField.setText("");
                    drawText();
                }
            }
        });
        drawText();
    }

    public void drawText() {

        for (int i = 0; i < message.size(); ++i) {
            JLabel label = getModifier(message.get(i));
            if (i == 0)
                label.setBounds(50, 75, 1100, 50);
            else
                label.setBounds(50, 75 + (i * 25), 1100, 50);
            add(label);
        }
        repaint();
    }
    public void loadContactList() {
        final JScrollPane scroll = new JScrollPane();
    }

    private JLabel getModifier(String text) {
        JLabel j = new JLabel(text);
        j.setFont(j.getFont().deriveFont(20f));
        j.setForeground(Color.white);
        return j;
    }

    public static ChatPanel getInstance() {
        return instance;
    }
}
