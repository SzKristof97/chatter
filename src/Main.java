import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import client.Client;
import commands.*;
import gui.MainGUI;
import managers.CommandManager;
import server.Server;

/**
 * Main class of the project
 */
public class Main {

    /**
     * Main method of the project
     * @param args arguments of the program
     */
    public static void main(String[] args) {
        new Main().run();
    }

    /**
     * Run the program
     */
    private void run(){
        CommandManager.getInstance().registerCommand(new KickCommand());
        CommandManager.getInstance().registerCommand(new PoPCommand());
        CommandManager.getInstance().registerCommand(new ClearCommand());

        MainGUI gui = MainGUI.getInstance();
        gui.setVisible(true);

        int choice = JOptionPane.showConfirmDialog(null, "Run a server = Yes\nor\nConnect to a server = No", "Server or client?", JOptionPane.YES_NO_OPTION);
        
        if(choice == JOptionPane.YES_OPTION){
            String port = JOptionPane.showInputDialog("Enter the port number");
            Server server = new Server(Integer.parseInt(port));
            server.start();

            Socket socket;
            try {
                socket = new Socket("localhost", Integer.parseInt(port));
                Client client = new Client(socket);
                client.Listen();

                gui.SetClient(client);
            } catch (NumberFormatException | IOException e) {
                JOptionPane.showMessageDialog(null, "Could not connect to the server");
            }
        }else{

            String ip = JOptionPane.showInputDialog("Enter the server IP");
            String port = JOptionPane.showInputDialog("Enter the server port");

            Socket socket;
            try {
                socket = new Socket(ip, Integer.parseInt(port));
                Client client = new Client(socket);
                client.Listen();

                gui.SetClient(client);
            } catch (NumberFormatException | IOException e) {
                JOptionPane.showMessageDialog(null, "Could not connect to the server");
            }
        }
    }
}
