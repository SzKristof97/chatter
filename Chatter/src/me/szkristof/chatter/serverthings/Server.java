package me.szkristof.chatter.serverthings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import me.szkristof.chatter.managers.ConsoleManager;

public class Server {
    
    //#region Propertes
    private ServerSocket serverSocket;
    //#endregion
    
    //#region Constructors

    /**
     * Creates a new instance of the Server class.
     * @param serverSettings The settings for the server.
     */
    public Server(int port) {
        //#region Initialize server

        try {
            serverSocket = new ServerSocket(port);
        } catch (NumberFormatException | IOException e) {
            ConsoleManager.WriteMessage("Failed to initialize server.");
            serverSocket = null;
        }

        //#endregion
    }
    //#endregion

    //#region Methods

    /**
     * Starts the server.
     */
    public void Start() {
        if(serverSocket == null) {
            ConsoleManager.WriteMessage("Failed to start server.");
            return;
        }

        while(!serverSocket.isClosed()){
            try{
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

                ConsoleManager.WriteMessage("Client connected.");
            }catch(Exception ex){
                ConsoleManager.WriteMessage("Failed to accept client.");
            }
        }
    }

    /**
     * Stops the server.
     */
    public void Stop() {
        try {
            if(serverSocket == null || serverSocket.isClosed())
            {
                ConsoleManager.WriteMessage("The server is not running or already stopped!");
                return;
            }

            serverSocket.close();
        } catch (IOException e) {
            ConsoleManager.WriteMessage("Failed to stop server.");
        }
    }
    //#endregion
}
