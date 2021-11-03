package qc.veko.chat.client.manager;

import qc.veko.chat.client.panels.LoginPanel;
import qc.veko.chat.client.panels.Panel;
import qc.veko.easyswing.EasySwing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigAndSaveManager extends EasySwing {
    private final String OS = (System.getProperty("os.name")).toUpperCase();  //The OS used by the user
    private String dataFolder = getOsDataFolder();  //Folder were all files are located for this software

    private String name;


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
                    String id = child.getName().replace(".txt", "");
                    //Verification if the file is the contact list or saved conversations
                    if (id.equals("contact"))
                        readContactList();
                    else if (id.equals("settings"))
                        loadConfig();
                    else
                        readChatInformations(id);
                    if (name == null) {
                        getFrame().setDefaultPanel(new LoginPanel()).setFrameResolution(1280, 720).setFrameTitle("Your Page Title");
                        launch();
                    }
                }
            }
        } else {
            mainDirectory.mkdir();
            mainDirectory.setWritable(true);

            getFrame().setDefaultPanel(new LoginPanel()).setFrameResolution(1280, 720).setFrameTitle("Your Page Title");
            launch();
        }
    }

    //Loading all contacts in the contact list
    private void readContactList() throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File(dataFolder + "contact" + ".txt")));
        String line;
        //Reading contac in the list to save them in cache
        while ((line = file.readLine()) != null) {
            Panel.contact.add(line);
        }
    }

    public void loadConfig() throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File(dataFolder + "settings" + ".txt")));
        //Reading settings in the list to save them in cache
        String line;
        while ((line = file.readLine()) != null) {
            if (line.startsWith("name : ")) {
                name = line.replace("name : ", "");
                System.out.println(name);
            }
        }
        getFrame().setDefaultPanel(new Panel()).setFrameResolution(1280, 720).setFrameTitle(name);
        launch();
    }

    /**
     *  Create new lines or modify existing lines in the configuration file
     * @param categorie the categorie chosen to modify or create
     * @param data the data of the categorie
     * @throws IOException Throws if any error occur (like idk man)
     */
    public void addConfigLine(String categorie, String data) throws IOException {
        try {
            File file = verificationIfFileExist(dataFolder + "settings" + ".txt");
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            String line = raf.readLine();
            if (line != null)
                while (!line.startsWith(categorie)) {
                    line = raf.readLine();
                }
            raf.writeBytes(categorie + " : " + data + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Adding contact to the list to save them
    public void addContactToList(String name) throws IOException {
        Writer output;
        //getting the output to write in a file when modifications are needed
        //if the file does not exist then a file is created
        try {
            output = new BufferedWriter(new FileWriter(dataFolder + "contact" + ".txt", true));
        } catch(FileNotFoundException e) {
            File chatFile = new File(dataFolder + "contact" + ".txt");
            chatFile.createNewFile();
            output = new BufferedWriter(new FileWriter(dataFolder + "contact" + ".txt", true));
        }
        //Writting the line needed and closing the file to save it
        output.write(name + "\n");
        output.close();
    }

    //Reading chat informations per files to save them in cache for the software
    private void readChatInformations(String name) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File(dataFolder + name + ".txt")));
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
            output = new BufferedWriter(new FileWriter(dataFolder + name + ".txt", true));
        } catch(FileNotFoundException e) {
            File chatFile = new File(dataFolder + name + ".txt");
            chatFile.createNewFile();
            output = new BufferedWriter(new FileWriter(dataFolder + name + ".txt", true));
        }
        //Writting the line needed and closing the file to save it
        output.write(message + "\n");
        output.close();
    }

    private File verificationIfFileExist(String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        return file;
    }

}
