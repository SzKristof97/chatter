package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import enums.Permission;
import gui.MainGUI;
import managers.CommandManager;
import managers.XmlManager;
import models.User;

public class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public User user;

    public static List<ClientHandler> clients = new ArrayList<ClientHandler>();

    /**
     * Create a new instance of ClientHandler
     * @param socket The socket of the client
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));
            
            int id = 0;
            for (ClientHandler client : clients) {
                if (client.user.getId() == id) {
                    id++;
                }
            }

            user = new User(id, "", Permission.USER);
            String username = reader.readLine();
            user.setUsername(username);

            clients.add(this);
            Broadcast("Server", username + " has joined the server");

            // Search for this user in XmlManager
            // If it is found, set the permission to USER
            // If it is not found, add it to the XmlManager
            User foundUser = XmlManager.SearchUser(username);
            
            
            if (foundUser != null) {
                user.setPermission(foundUser.getPermission());

            } else {
                XmlManager.SaveUsers(clients);
            }

        } catch (Exception e) {
            MainGUI.getInstance().Message("Error creating the reader/writer for the client: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try {
                messageFromClient = reader.readLine();
                if(messageFromClient == null) continue;

                if(messageFromClient.startsWith("/")){
                    String cmdName = messageFromClient.split(" ")[0].replace("/", "");
                    // get the command arguments if there are any without the cmd name and create a String[] array

                    String[] args;
                    if(messageFromClient.split(" ").length < 1){
                        args = null;
                    }else{
                        args = new String[messageFromClient.split(" ").length - 1];
                        for (int i = 1; i < messageFromClient.split(" ").length; i++) {
                            args[i - 1] = messageFromClient.split(" ")[i];
                        }
                    }
                    String result = CommandManager.getInstance().executeCommand(user, cmdName, args);
                    if(result == null){
                        Send("Server", "Unknown command: " + cmdName, user.getId());
                    }else{
                        Send("Server", result, user.getId());
                    }
                    continue;
                }

                Broadcast("("+user.getId()+")["+user.getPermission().toString()+"]" + user.getUsername(), messageFromClient);
            } catch (Exception e) {
                MainGUI.getInstance().Message("Error reading the message from the client: " + e.getMessage() + "\n" + e.getStackTrace());
                stop(user.getId());
                break;
            }
        }
    }

     /**
     * Send a message to all clients
     * @param sender The sender of the message
     * @param message The message
     */
    public static void Broadcast(String sender, String message) {
        for(ClientHandler client : clients) {
            try {
                client.writer.write("@Username=" + sender + "@Message=" + message);
                client.writer.newLine();
                client.writer.flush();
            } catch (Exception e) {
                MainGUI.getInstance().Message("Error sending the message to the client: " + e.getMessage());
                stop(client.user.getId());
            }
        }
    }

    /**
     * Send a message to a specific client
     * @param sender The sender of the message
     * @param message The message
     * @param clientId The id of the client
     * @return True if the message was sent, false otherwise
     */
    public static boolean Send(String sender, String message, int clientId) {
        ClientHandler client = clients.stream().filter(c -> c.user.getId() == clientId).findFirst().orElse(null);
        if(client == null) return false;

        try {
            client.writer.write("@Username=" + sender + "@Message=" + message);
            client.writer.newLine();
            client.writer.flush();
        } catch (Exception e) {
            MainGUI.getInstance().Message("Error sending the message to the client: " + e.getMessage());
            stop(client.user.getId());
            return false;
        }

        return true;
    }

    /**
     * Stop the client handler
     */
    public static void stop(int clientId) {
       ClientHandler client = clients.stream()
                                     .filter(c -> c.user.getId() == clientId)
                                     .findFirst()
                                     .orElse(null);

        if(client == null) return;

        try{
            client.socket.close();
            client.reader.close();
            client.writer.close();

            clients.remove(client);
            Broadcast("Server", client.user.getUsername() + " has left the server");
        } catch (Exception e) {
            MainGUI.getInstance().Message("Error stopping the client: " + e.getMessage());
        }
    }
}
