package me.szkristof.chatter.clientthings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import me.szkristof.chatter.managers.ConsoleManager;
import me.szkristof.chatter.managers.ClientManager;

/**
 * Client
 */
public class Client {

    //#region Propertes

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private ClientManager currentClient;

    //#endregion

    //#region Constructors

    /**
     * Creates a new instance of the Client class.
     * @param socket The socket of the client.
     */
    public Client(Socket socket) {
        this.socket = socket;
        currentClient = new ClientManager();
        try{
            boolean isLoaded = currentClient.Load();
            if(!isLoaded){
                boolean isCreated = false;

                while (isCreated == false) {
                    isCreated = currentClient.CreateUser(ConsoleManager.ReadString("Enter your name: "));
                }
                ConsoleManager.Clear();
            }else{
                ConsoleManager.WriteMessage("Your user name is: " + currentClient.GetUser().GetName() + "\n");
                boolean wantToChange = ConsoleManager.ReadBoolean("Do you want to change your name? (y/n)", "y", "n");
                if(wantToChange){
                    String newName = ConsoleManager.ReadString("Enter your new name: ");
                    currentClient.GetUser().SetName(newName);
                    ConsoleManager.Clear();
                }

                ConsoleManager.Clear();
            }

            reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));

            ConsoleManager.WriteMessage("Send a blank message to exit.\n");

            SendWelcomeMessage();
        }catch(Exception e){
            ConsoleManager.WriteMessage("Failed to initialize client handler.\n");
            stop(socket, reader, writer);
        }
    }

    //#endregion

    /**
     * Stop the client
     * @param socket2 The socket of the client.
     * @param reader2 The reader of the client.
     * @param writer2 The writer of the client.
     */
    private void stop(Socket socket2, BufferedReader reader2, BufferedWriter writer2) {
        try{
            
            if(reader2 != null){
                reader2.close();
            }

            if(writer2 != null){
                writer2.close();
            }

            if(socket2 != null){
                socket2.close();
            }
        }catch(Exception e){
            ConsoleManager.WriteMessage("Failed to close client handler.");
        }
    }

    //#region
    /**
     * Send the username to the server
     */
    public void SendWelcomeMessage(){
        try{
            writer.write(currentClient.GetUser().GetName());
            writer.newLine();
            writer.flush();
        }catch(Exception e){
            ConsoleManager.WriteMessage("Failed to send username to server.");
            stop(socket, reader, writer);
        }
    }

    /**
     * Send a message with the username to the server
     */
    public void SendMessage(){
        try{
            String message = ConsoleManager.ReadString("");
            if(message == null || message.isEmpty() || message.equals("")){
                stop(socket, reader, writer);
                return;
            }

            writer.write(message);
            writer.newLine();
            writer.flush();
        }catch(Exception e){
            ConsoleManager.WriteMessage("Failed to send message to server.");
            stop(socket, reader, writer);
        }
    }
    
    /**
     * Listen for messages from the server
     */
    public void ListenForMessages(){
        try{
            new Thread(new Runnable(){
                @Override
                public void run(){
                    while(!socket.isClosed()){
                        try{
                            String message = reader.readLine();
                            if(message.startsWith("@Username=")){
                                String username = message.substring(10, message.indexOf("@Message="));
                                if(username.equals(currentClient.GetUser().GetName())){
                                    continue;
                                }

                                String message2 = message.substring(message.indexOf("@Message=") + 9);
                                ConsoleManager.WriteMessage(username + ": " + message2 + "\n");
                            }else{
                                ConsoleManager.WriteMessage("Cannot identify the sender of the message.\n");
                            }
                        }catch(Exception e){
                            ConsoleManager.WriteMessage("Failed to read a message from server.\n");
                            stop(socket, reader, writer);
                        }
                    }
                }
            }).start();
        }catch(Exception e){
            ConsoleManager.WriteMessage("Failed to listen for messages from server.\n");
            stop(socket, reader, writer);
        }
    }
    //#endregion
}