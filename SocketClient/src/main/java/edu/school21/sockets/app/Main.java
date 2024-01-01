package edu.school21.sockets.app;

import edu.school21.sockets.client.Client;

public class Main {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0].substring(14));
        Client client = new Client();
        client.start(port);
    }
}
