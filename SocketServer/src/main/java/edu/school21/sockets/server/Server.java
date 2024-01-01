package edu.school21.sockets.server;

import edu.school21.sockets.services.ChatroomService;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class Server {
    private ServerSocket serverSocket;
    private UsersService usersService;
    private MessageService messageService;
    private ChatroomService chatroomService;
    private static List<ClientHandlerThread> clients = new ArrayList<>();

    @Autowired
    public Server(UsersService usersService, MessageService messageService, ChatroomService chatroomService) {
        this.usersService = usersService;
        this.messageService = messageService;
        this.chatroomService = chatroomService;
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            int i = 1;
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("User" + i++ + " connected to the chat");
                ClientHandlerThread thread = new ClientHandlerThread(socket, usersService, messageService, chatroomService);
                clients.add(thread);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessageToChats(Long id, String username, String text) {
        for (ClientHandlerThread client : clients) {
            if (client.getChatId() != null && client.getChatId().equals(id)) {
                client.sendMessage(username, text);
            }
        }
    }
}
