package qc.veko.chat.client.manager;

import qc.veko.chat.client.panels.Panel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigAndSaveManager {
    private final String OS = (System.getProperty("os.name")).toUpperCase();
    private String dataFolder = getOsDataFolder();

    private String getOsDataFolder() {
        String dataFolder;
        if (OS.contains("WIN")) {
            dataFolder = System.getenv("APPDATA") + "\\" + "WeOpenChat" + "//";
        }
        else {
            dataFolder = System.getProperty("user.home") + "/WeOpenChat//";
        }
        return dataFolder;
    }

    public void loadMessage() throws IOException {
        File mainDirectory = new File(dataFolder);
        if(mainDirectory.exists()) {
            File[] directoryListing = mainDirectory.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    String id = child.getName().replace(".yml", "");
                    if (id.equals("contact"))
                        readContactList();
                    readChatInformations(id);
                }
            }
        } else {
            mainDirectory.mkdir();
            mainDirectory.setWritable(true);
        }
    }

    private void readContactList() throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File(dataFolder + "contact" + ".yml")));
        String line;
        while ((line = file.readLine()) != null) {
            Panel.contact.add(line);
        }
    }

    public void addContactToList(String name) throws IOException {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(dataFolder + "contact" + ".yml", true));
        } catch(FileNotFoundException e) {
            File chatFile = new File(dataFolder + "contact" + ".yml");
            chatFile.createNewFile();
            output = new BufferedWriter(new FileWriter(dataFolder + "contact" + ".yml", true));
        }
        output.write(name + "\n");
        output.close();
    }

    private void readChatInformations(String name) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File(dataFolder + name + ".yml")));
        String line;
        List<String> messageList = new ArrayList<>();
        while ((line = file.readLine()) != null) {
            messageList.add(line);
        }
        Panel.message.put(name, messageList);
    }

    public void addChatInformations(String name, String message) throws IOException {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(dataFolder + name + ".yml", true));
        } catch(FileNotFoundException e) {
            File chatFile = new File(dataFolder + name + ".yml");
            chatFile.createNewFile();
            output = new BufferedWriter(new FileWriter(dataFolder + name + ".yml", true));
        }
        output.write(message + "\n");
        output.close();
    }


}
