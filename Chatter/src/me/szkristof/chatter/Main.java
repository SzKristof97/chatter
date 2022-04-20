package me.szkristof.chatter;

import java.io.IOException;
import java.net.Socket;

import me.szkristof.chatter.clientthings.Client;
import me.szkristof.chatter.enums.MENU;
import me.szkristof.chatter.managers.ConsoleManager;
import me.szkristof.chatter.serverthings.Server;

/**
 * Main class.
 */
public class Main {

    //#region Properties
    private static Server server;
    //#endregion

    //#region Main Methods
    /**
     * The main entry point of the program.
     * @param args The arguments.
     */
    public static void main(String[] args) {
        new Main().run();
    }

    /**
     *  The main loop of the program.
     */
    private void run() {
        // Create menu system
        MENU menu = MENU.MAINMENU;

        // Show different menus
        boolean isRunning = true;

        while (isRunning) {
            ConsoleManager.Clear();
            switch (menu) {
                default:
                case MAINMENU:
                    menu = ShowMainMenu();
                    break;
                case CLIENTMENU:
                    menu = ShowClientMenu();
                    break;
                case SERVERMENU:
                    menu = ShowServerMenu();
                    break;
                case EXIT:
                    isRunning = false;
                    break;
            }
        }
    }
    //#endregion

    //#region ShowMenu
    /**
     * Show the main menu.
     * @return Next menu.
     */
    private MENU ShowMainMenu() {
        ConsoleManager.WriteMessage("===== [ Main menu ] =====\n");
        ConsoleManager.WriteMessage("1. Connect to a server\n");
        ConsoleManager.WriteMessage("2. Start your own server\n");
        ConsoleManager.WriteMessage("3. Exit\n");

        int choice = ConsoleManager.ReadInt("Please choose an option: ");

        switch (choice) {
            case 1:
                return MENU.CLIENTMENU;
            case 2:
                return MENU.SERVERMENU;
            case 3:
                return MENU.EXIT;
            default:
                ConsoleManager.ReadString("\nInvalid option. Press enter to continue.");
                return MENU.MAINMENU;
        }
    }

    /**
     * Show the client menu.
     * @return Next menu.
     */
    private MENU ShowClientMenu() {
        String ipAddress = ConsoleManager.ReadString("Please enter the server's IP address: ");
        int port = ConsoleManager.ReadInt("Please enter the server's port: ");

        if(ipAddress == null || ipAddress.isEmpty()) {
            ConsoleManager.WriteMessage("\nInvalid IP address. Press enter to continue.\n");
            ConsoleManager.ReadString("Press enter to continue.");
            return MENU.MAINMENU;
        }

        if(port <= 0) {
            ConsoleManager.WriteMessage("\nInvalid port. Press enter to continue.\n");
            ConsoleManager.ReadString("Press enter to continue.");
            return MENU.MAINMENU;
        }

        if(!ConsoleManager.ReadBoolean("\nConnect to " + ipAddress + ":" + port + "? (y/n): ", "y", "n")) {
            return MENU.MAINMENU;
        }

        try {
            ConsoleManager.Clear();
            Socket socket = new Socket(ipAddress, port);
            Client client = new Client(socket);
            client.ListenForMessages();

            while(!socket.isClosed()){
                client.SendMessage();
            }
        } catch (IOException e) {
            ConsoleManager.WriteMessage("\nCould not connect to the server.\n");
            ConsoleManager.ReadString("Press enter to continue.");
            return MENU.MAINMENU;
        }

        return MENU.MAINMENU;
    }
    
    /**
     * Show the server menu.
     * @return Next menu.
     */
    private MENU ShowServerMenu() {
        ConsoleManager.WriteMessage("===== [ Server menu ] =====\n");
        ConsoleManager.WriteMessage("1. Start server\n");
        ConsoleManager.WriteMessage("2. Stop server\n");
        ConsoleManager.WriteMessage("3. Return to main menu\n");

        int choice = ConsoleManager.ReadInt("Please choose an option: ");

        switch (choice) {
            case 1:
                return StartServer();
            case 2:
                return StopServer();
            case 3:
                return MENU.MAINMENU;
            default:
                ConsoleManager.ReadString("\nInvalid option. \nPress enter to continue.");
                return MENU.SERVERMENU;
        }
    }

    /**
     * Start the server.
     * @return Next menu.
     */
    private MENU StartServer() {
        ConsoleManager.Clear();
        if(server != null && server.isRunning){
            ConsoleManager.WriteMessage("\nThe server is already running.\n");
            ConsoleManager.ReadString("Press enter to continue.");
            return MENU.SERVERMENU;
        }
        
        int port = ConsoleManager.ReadInt("Please enter the port: ");

        if(port <= 0) {
            ConsoleManager.WriteMessage("\nInvalid port. Press enter to continue.\n");
            ConsoleManager.ReadString("Press enter to continue.");
            return MENU.SERVERMENU;
        }

        if(!ConsoleManager.ReadBoolean("\nStart server on port " + port + "? (y/n): ", "y", "n")) {
            return MENU.SERVERMENU;
        }

        server = new Server(port);
        server.Start();

        return MENU.SERVERMENU;
    }
    
    /**
     * Stop the server.
     * @return Next menu.
     */
    private MENU StopServer() {
        ConsoleManager.Clear();
        if(server == null || !server.isRunning){
            ConsoleManager.WriteMessage("\nThe server is not running.\n");
            ConsoleManager.ReadString("Press enter to continue.");
            return MENU.SERVERMENU;
        }

        if(!ConsoleManager.ReadBoolean("\nStop the server? (y/n): ", "y", "n")) {
            return MENU.SERVERMENU;
        }

        server.Stop();
        server = null;

        return MENU.SERVERMENU;
    }
    //#endregion
}