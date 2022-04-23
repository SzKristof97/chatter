package gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import client.Client;
import models.User;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class MainGUI extends JFrame {
    
    //#region Properties
    private static final long serialVersionUID = 1L;
    private static MainGUI instance;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;

    private JTextPane console;
    private JScrollPane scrollPane;
    private JTextField input;

    private Client client;

    public static MainGUI getInstance() {
        if(instance == null) {
            instance = new MainGUI();
        }
        return instance;
    }
    //#endregion

    //#region Constructors
    /**
     * Create the frame.
     * Need to set it visible to run!
     */
    public MainGUI() {
        setTitle("Chatter");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBackground(new Color(15,15,15));

        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        Initialize();
        
    }
    //#endregion

    //#region Methods

    /**
     * Create contents of the frame.
     */
    private void Initialize(){
        //#region Initialize properties
        console = new JTextPane();
        scrollPane = new JScrollPane(console);
        input = new JTextField();
        //#endregion

        //#region Set properties
        console.setEditable(false);
        console.setBackground(new Color(15,15,15));
        console.setForeground(Color.green);

        console.setText("");
        console.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        input.setBackground(new Color(30,30,30));
        input.setForeground(Color.green);

        input.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        input.addActionListener(e -> {
            String text = input.getText().trim();
            if(text == null || text.isEmpty()) return;
            if(client == null) return;
            
            client.Send(text);
            input.selectAll();
        });
        //#endregion

        //#region Add components
        add(input, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        //#endregion
    }

    /**
     * Set the client to use
     * @param client The client to use
     * @return True if the client was set, false if it was not
     */
    public boolean SetClient(Client client){
        if(client == null) return false;
        this.client = client;
        return true;
    }
    /**
     * Prints a message to the console
     * @param text The message to print
     * @return True if the message was printed, false if it was not
     */
    public boolean Message(String text){
        if(text.length() > 0){
            console.setText(console.getText() + "Server >> " + text + "\n");
            return true;
        }
        return false;
    }

    /**
     * Prints a message to the console
     * @param user The user who sent the message
     * @param text The message to print
     * @return True if the message was printed, false if it was not
     */
    public boolean Message(User user, String text){
        if(text.length() > 0){
            console.setText(console.getText() + user.getUsername() + " >> " + text + "\n");
            return true;
        }
        return false;
    }

    /**
     * Prints a message to the console
     * @param user The user who sent the message
     * @param text The message to print
     * @return True if the message was printed, false if it was not
     */
    public boolean Message(String user, String text){
        if(text.length() > 0){
            console.setText(console.getText() + user + " >> " + text + "\n");
            return true;
        }
        return false;
    }

    /**
     * Scrolls the console to the bottom
     */
    public void ScrollToBottom(){
        console.setCaretPosition(console.getDocument().getLength());
    }

    /**
     * Clears the console
     */
    public void Clear(){
        console.setText("");
    }
    //#endregion
}
