package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import gui.MainGUI;

/**
 * This class is the client of the application
 */
public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Client(Socket socket) {
        this.socket = socket;

        try{
            reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));

            // Ask the user for his username
            String username = JOptionPane.showInputDialog("Enter your username");

            // Send the username to the server
            writer.write(username);
            writer.newLine();
            writer.flush();

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "An error has occured while creating the client!\n"+e.getMessage(), "Client Error", JOptionPane.ERROR_MESSAGE);
            stop();
        }
    }

    /**
     * Stop the client
     */
    private void stop() {
        try {
            if(socket != null)
                socket.close();
            if(reader != null)
                reader.close();
            if(writer != null)
                writer.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error has occured while closing the client!\n"+e.getMessage(), "Client Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Send a message to the server
     */
    public void Send(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error has occured while sending the message!\n"+e.getMessage(), "Client Error", JOptionPane.ERROR_MESSAGE);
            stop();
        }
    }

    /**
     * Listen for messages from the server
     */
    public void Listen() {
        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!socket.isClosed()){
                        try{
                            String messageFromServer = reader.readLine();

                            // Get the username (from @Username=) and message (from @Message=) from the server
                            String username = messageFromServer.substring(10, messageFromServer.indexOf("@Message="));
                            String message = messageFromServer.substring(messageFromServer.indexOf("@Message=") + 9);

                            if(message.trim().startsWith("cmd:")){
                                String cmdName = message.split(" ")[0].replace("cmd:", "");
                                if(cmdName.equals("clear")){
                                    MainGUI.getInstance().Clear();
                                    continue;
                                }
                            }
                            // Display the message
                            MainGUI.getInstance().Message(username, message);
                        }catch(Exception e){
                            JOptionPane.showMessageDialog(null, "An error has occured while listening for messages!\n"+e.getMessage(), "Client Error", JOptionPane.ERROR_MESSAGE);
                            stop();
                            break;
                        }
                    }
                }
            }).start();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "An error has occured while listening for messages!\n"+e.getMessage(), "Client Error", JOptionPane.ERROR_MESSAGE);
            stop();
        }
    }
}
