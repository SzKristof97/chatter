package server;

import java.net.ServerSocket;
import java.net.Socket;

import gui.MainGUI;

/**
 * The Server class is used to represent a server.
 */
public class Server {

    private static Server instance;
    public static Server getInstance() {
        if(instance == null) {
            instance = new Server(1234);
        }
        return instance;
    }

    private ServerSocket serverSocket;

    /**
     * Create the Server.
     */
    public Server(int port) {
        instance = this;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            MainGUI.getInstance().Message(e.getMessage() + "\n\n" + e.getStackTrace());
        }
    }

    /**
     * Start the server
     */
    public void start() {
        MainGUI.getInstance().Message("Starting the server...");

        if(serverSocket == null)
        {
            MainGUI.getInstance().Message("The server socket is null!");
            return;
        }

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    while(!serverSocket.isClosed()) {
                        Socket socket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(socket);

                        new Thread(clientHandler).start();
                    }
                } catch (Exception e) {
                    MainGUI.getInstance().Message(e.getMessage() + "\n\n" + e.getStackTrace());
                }
            }
        }).start();

        MainGUI.getInstance().Message("Server started!");
    }

    /**
     * Stop the server
     */
    public void stop() {
        MainGUI.getInstance().Message("Stopping the server...");

        if(serverSocket == null)
        {
            MainGUI.getInstance().Message("The server socket is null!");
            return;
        }

        if(IsRunning()){
            MainGUI.getInstance().Message("The server is already stopped!");
            return;
        }

        try {
            serverSocket.close();
        } catch (Exception e) {
            MainGUI.getInstance().Message(e.getMessage() + "\n\n" + e.getStackTrace());
        }

        MainGUI.getInstance().Message("Server stopped!");
    }

    /**
     * Check if the server is running
     * @return true if the server is running, otherwise false
     */
    public boolean IsRunning(){
        return !serverSocket.isClosed();
    }
}
