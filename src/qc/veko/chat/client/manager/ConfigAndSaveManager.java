package qc.veko.chat.client.manager;

import qc.veko.chat.client.panels.Panel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigAndSaveManager {
    private final String OS = (System.getProperty("os.name")).toUpperCase();  //The OS used by the user
    private String dataFolder = getOsDataFolder();  //Folder were all files are located for this software


    //Getting the os use by the user to set the main save directory
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

    //Loading all message in the main directory
    public void loadMessage() throws IOException {
        File mainDirectory = new File(dataFolder);
        if(mainDirectory.exists()) {
            File[] directoryListing = mainDirectory.listFiles();
            if (directoryListing != null) {
                //Looking for every file in the main folder
                for (File child : directoryListing) {
                    String id = child.getName().replace(".yml", "");
                    //Verification if the file is the contact list or saved conversations
                    if (id.equals("contact"))
                        readContactList();
                    else
                        readChatInformations(id);
                }
            }
        } else {
            mainDirectory.mkdir();
            mainDirectory.setWritable(true);
        }
    }

    //Loading all contacts in the contact list
    private void readContactList() throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File(dataFolder + "contact" + ".yml")));
        String line;
        //Reading contac in the list to save them in cache
        while ((line = file.readLine()) != null) {
            Panel.contact.add(line);
        }
    }

    //Adding contact to the list to save them
    public void addContactToList(String name) throws IOException {
        Writer output;
        //getting the output to write in a file when modifications are needed
        //if the file does not exist then a file is created
        try {
            output = new BufferedWriter(new FileWriter(dataFolder + "contact" + ".yml", true));
        } catch(FileNotFoundException e) {
            File chatFile = new File(dataFolder + "contact" + ".yml");
            chatFile.createNewFile();
            output = new BufferedWriter(new FileWriter(dataFolder + "contact" + ".yml", true));
        }
        //Writting the line needed and closing the file to save it
        output.write(name + "\n");
        output.close();
    }

    //Reading chat informations per files to save them in cache for the software
    private void readChatInformations(String name) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File(dataFolder + name + ".yml")));
        String line;
        //Adding every line of chat in cache
        List<String> messageList = new ArrayList<>();
        while ((line = file.readLine()) != null) {
            messageList.add(line);
        }
        Panel.message.put(name, messageList);
    }

    //Adding chat message in the good file to save every conversations in the main directory
    public void addChatInformations(String name, String message) throws IOException {
        Writer output;
        //getting the output to write in a file when modifications are needed
        //if the file does not exist then a file is created
        try {
            output = new BufferedWriter(new FileWriter(dataFolder + name + ".yml", true));
        } catch(FileNotFoundException e) {
            File chatFile = new File(dataFolder + name + ".yml");
            chatFile.createNewFile();
            output = new BufferedWriter(new FileWriter(dataFolder + name + ".yml", true));
        }
        //Writting the line needed and closing the file to save it
        output.write(message + "\n");
        output.close();
    }


}
