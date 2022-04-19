package me.szkristof.chatter;

import java.io.IOException;
import java.net.Socket;

import me.szkristof.chatter.clientthings.Client;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 1234);
            Client client = new Client(socket);
            client.ListenForMessages();
            client.SendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}