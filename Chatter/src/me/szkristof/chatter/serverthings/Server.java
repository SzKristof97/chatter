package me.szkristof.chatter.serverthings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import me.szkristof.chatter.managers.ConsoleManager;

public class Server {
    
    //#region Propertes
    private ServerSocket serverSocket;

    public boolean isRunning = false;
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
            ConsoleManager.WriteMessage("Failed to start server.\n");
            ConsoleManager.ReadString("Press enter to continue...");
            return;
        }

        new Thread(new Runnable(){
            @Override
            public void run(){
                while(!serverSocket.isClosed()){
                    try{
                        Socket socket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(socket);
        
                        Thread thread = new Thread(clientHandler);
                        thread.start();
        
                        isRunning = true;
                        ConsoleManager.WriteMessage("Client connected.\n");
                    }catch(Exception ex){
                        ConsoleManager.WriteMessage("Failed to accept client.\n");
                        isRunning = false;
                    }
                }
            }
        }).start();
    }

    /**
     * Stops the server.
     */
    public void Stop() {
        try {
            if(serverSocket == null || serverSocket.isClosed())
            {
                ConsoleManager.WriteMessage("The server is not running or already stopped!\n");
                ConsoleManager.ReadString("Press enter to continue...");
                return;
            }
            
            serverSocket.close();
            isRunning = false;
        } catch (IOException e) {
            ConsoleManager.WriteMessage("Failed to stop server.\n");
            ConsoleManager.ReadString("Press enter to continue...");
            isRunning = false;
        }
    }
    //#endregion
}
