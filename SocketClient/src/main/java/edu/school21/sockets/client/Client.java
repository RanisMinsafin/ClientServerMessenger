package edu.school21.sockets.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private ObjectMapper mapper;

    public void start(int port) {
        try {
            socket = new Socket("localhost", port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            mapper = new ObjectMapper();
            ReadMessageThread read = new ReadMessageThread();
            WriteMessageThread write = new WriteMessageThread();

            read.start();
            write.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ReadMessageThread extends Thread {
        @Override
        public void run() {
            try {
                readMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void readMessage() throws IOException {
            String serverMessage;

            while (true) {
                serverMessage = mapper.readValue(input.readLine(), String.class);
                System.out.println(serverMessage);

                if (serverMessage.equals("You have left the chat!") ||
                        serverMessage.equals("Incorrect password!") ||
                        serverMessage.equals("Incorrect action!")) {
                    closeConnection();
                    break;
                }
            }
        }

    }

    private class WriteMessageThread extends Thread {
        @Override
        public void run() {
            try {
                writeMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeMessage() throws IOException {
            try (Scanner scanner = new Scanner(System.in)) {
                while (socket.isConnected()) {
                    String clientMessage = mapper.writeValueAsString(scanner.nextLine());
                    output.println(clientMessage);
                }
            }
        }
    }

    private void closeConnection() {
        try {
            input.close();
            output.close();
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
