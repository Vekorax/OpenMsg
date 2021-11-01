package qc.veko.chat.client.panels;

import qc.veko.chat.client.Chat;
import qc.veko.chat.client.socket.Sockets;
import qc.veko.easyswing.engine.EasyFrame;
import qc.veko.easyswing.engine.EasyPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Panel extends EasyPanel {

    private JTextArea chatWindow;
    private JTextField input;
    private static Panel instance;
    public static String contactChosen = "";
    JPanel buttonsPane;

    public static List<String> contact = new LinkedList<String>();
    public static Map<String, List<String>> message = new HashMap();

    public Panel() {
        instance = this;
        if (!contact.contains("Add Contact"))
            contact.add("Add Contact");

        JPanel leftPane = new JPanel(new GridBagLayout());
        JPanel rightPane = new JPanel(new GridBagLayout());

        chatWindow = new JTextArea(20, 40);
        chatWindow.setText("Waiting to start chat...");
        chatWindow.setLineWrap(true);
        chatWindow.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                chatWindow.setEditable(true);
            }
            @Override
            public void focusGained(FocusEvent e) {
                chatWindow.setEditable(false);
            }
        });
        input = new JTextField(10);
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    Sockets.getInstance().send(input.getText());
                    chatWindow.setText(chatWindow.getText() + "\n" + "You : " + input.getText());
                    input.setText("");
                }
            }
        });
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1;
        rightPane.add(new JScrollPane(chatWindow, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        rightPane.add(input, gbc);

        buttonsPane = new JPanel(new GridLayout(8, 1));
        contact.forEach(contact -> {
            JButton button = new JButton(contact);
            button.addActionListener(buttonAction(button));
            buttonsPane.add(button);
        });

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        leftPane.add(buttonsPane, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(leftPane, gbc);

        gbc.gridx++;
        gbc.weighty = 1;
        gbc.weightx = 6;
        add(rightPane, gbc);

        addTextForNewWindow(contactChosen);
    }

    //Adding text in the chat box that is appropriate for every contact
    public void addTextForNewWindow(String name) {
        chatWindow.setText(name);
        if(contactChosen.equals(""))
            return;
        message.get(contactChosen).forEach(msg -> {
            chatWindow.setText(chatWindow.getText() + "\n" + msg);
        });
    }

    //Adding text in the chat box that is appropriate for every contact and a verification if the contact chosen is the one selected
    //by the user
    public void addText(String name, String message) {
        if(contactChosen.equals(""))
            return;
        if (name.equals(contactChosen))
            chatWindow.setText(chatWindow.getText() + "\n" + message);
        else
            addTextForNewWindow(name);
    }

    public static Panel getInstance() {
        return instance;
    }

    //JButton action in one method for easier reading
    private ActionListener buttonAction(JButton button) {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = button.getText();
                if (name.equals("Add Contact")) {
                    String text = JOptionPane.showInputDialog(null, "Enter Name of Button", "Name of Button", JOptionPane.INFORMATION_MESSAGE);
                    if (text == null)
                        return;
                    contact.add(text);
                    try {
                        Chat.getInstance().configAndSaveManager.addContactToList(text);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    EasyFrame.getInstance().setPanel(new Panel());
                } else {
                    contactChosen = name;
                    chatWindow.setText(name);
                    addTextForNewWindow(contactChosen);
                }
            }
        };
        return action;
    }

    //Actualise the panel for when new messages enter
    public void actualise(String message) {
        String uncomposedMessage[] = message.split(":");
        String name = uncomposedMessage[0].replace(" ", "");
        if (this.message.containsKey(name))
            this.message.get(name).add(message);
        else {
            List<String> newListOfMessage = new ArrayList<>();
            newListOfMessage.add(message);
            this.message.put(name, newListOfMessage);
        }
        try {
            Chat.getInstance().configAndSaveManager.addChatInformations(name, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addText(name, message);
    }


}
