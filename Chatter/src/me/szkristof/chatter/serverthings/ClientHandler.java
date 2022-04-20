package me.szkristof.chatter.serverthings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

import me.szkristof.chatter.managers.ConsoleManager;

public class ClientHandler implements Runnable{

    //#region Propertes
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private String username;

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    //#endregion

    //#region Constructors
    /**
     * Creates a new instance of the ClientHandler class.
     * @param socket The socket of the client.
     * @param serverSettings The settings for the server.
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;

        try{
            reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));
            username = reader.readLine();

            Broadcast(username, "has joined the server.");
            clientHandlers.add(this);
        }catch(Exception e){
            ConsoleManager.WriteMessage( "Failed to initialize client handler.\n");
            stop(socket, reader, writer);
        }
    }

    //#endregion

    //#region Methods
    /**
     * Starts the client handler.
     */
    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = reader.readLine();
                if(messageFromClient != null){
                    Broadcast(username, messageFromClient);
                }
            }catch(Exception e){
                ConsoleManager.WriteMessage( "Failed to read message from client.\n");
                ConsoleManager.WriteMessage( e.getMessage());
                stop(socket, reader, writer);
                break;
            }
        }
    }

    public void Broadcast(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try{
                clientHandler.writer.write("@Username=server@Message="+messageToSend);
                clientHandler.writer.newLine();
                clientHandler.writer.flush();
            }catch(Exception e){
                ConsoleManager.WriteMessage( "Failed to send message to client.\n");
                stop(clientHandler.socket, clientHandler.reader, clientHandler.writer);
            }
        }
    }

    public void Broadcast(String username, String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try{
                clientHandler.writer.write("@Username=" + username + "@Message=" + messageToSend);
                clientHandler.writer.newLine();
                clientHandler.writer.flush();
            }catch(Exception e){
                ConsoleManager.WriteMessage( "Failed to send message to client.\n");
                stop(clientHandler.socket, clientHandler.reader, clientHandler.writer);
            }
        }
    }

    /**
     * Stops the client handler.
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
            ConsoleManager.WriteMessage( "Failed to close client handler.\n");
        }
    }

    public void RemoveClientHandler(){
        clientHandlers.remove(this);
        Broadcast(username + " has left the server.");
    }
    //#endregion
}
