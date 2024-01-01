package edu.school21.sockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.ChatroomService;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;
import lombok.Data;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClientHandlerThread extends Thread {
    private Long userId;
    private Long chatId;
    private String username;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private ObjectMapper mapper;
    private UsersService usersService;
    private MessageService messageService;
    private ChatroomService chatroomService;

    public ClientHandlerThread(Socket socket, UsersService usersService,
                               MessageService messageService, ChatroomService chatroomService) throws IOException {
        this.socket = socket;
        this.usersService = usersService;
        this.messageService = messageService;
        this.chatroomService = chatroomService;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            getClientAction();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred during the authorization process.", e);
        }
    }

    private void getClientAction() throws IOException {
        sendToClient("Hello from Server!\n" + "1. SignIn\n2. SignUp\n3. Exit");
        String action = mapper.readValue(input.readLine(), String.class);
        switch (action) {
            case "1" -> signIn();
            case "2" -> signUp();
            case "3" -> closeClient();
            default -> {
                sendToClient("Incorrect action!");
                closeConnection();
            }
        }
    }

    private void closeClient() {
        sendToClient("You have left the chat!");
        closeConnection();
    }

    private void sendToClient(String text) {
        try {
            String message = mapper.writeValueAsString(text);
            output.println(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    void signUp() throws IOException {
        User user = getUser();
        usersService.signUp(user);
        userId = usersService.getUserId(username);
        roomMenu();
    }

    void signIn() throws IOException {
        User user = getUser();
        userId = usersService.getUserId(username);

        if (usersService.signIn(user)) {
            roomMenu();
        } else {
            sendToClient("Incorrect password!");
            closeConnection();
        }
    }

    private User getUser() throws IOException {
        User user = new User();
        sendToClient("Enter username:");
        username = mapper.readValue(input.readLine(), String.class);
        user.setUsername(username);
        sendToClient("Enter password:");
        String password = mapper.readValue(input.readLine(), String.class);
        user.setPassword(password);
        return user;
    }

    private void roomMenu() throws IOException {
        sendToClient(
                "1.\tCreate room\n" +
                        "2.\tChoose room\n" +
                        "3.\tExit"
        );
        String choice = mapper.readValue(input.readLine(), String.class);
        switch (choice) {
            case "1" -> createRoom();
            case "2" -> chooseRoom();
            case "3" -> closeClient();
            default -> {
                sendToClient("Incorrect action!");
                closeConnection();
            }
        }
    }

    private void createRoom() throws IOException {
        sendToClient("Enter chatroom name:");
        String name = mapper.readValue(input.readLine(), String.class);
        sendToClient("Enter chatroom password:");
        String password = mapper.readValue(input.readLine(), String.class);

        Chatroom chatroom = new Chatroom();
        chatroom.setName(name);
        chatroom.setOwnerId(userId);
        chatroom.setPassword(password);
        chatroomService.save(chatroom);
        Chatroom savedRoom = chatroomService.findByName(name).get();
        chatId = savedRoom.getId();
        startMessaging();
        sendToClient("Successful!");
    }

    private void chooseRoom() throws IOException {
        List<Chatroom> list = chatroomService.findAll();
        if (list.size() != 0) {
            int i = 0;
            for (Chatroom chatroom : list) {
                sendToClient(++i + ". " + chatroom.getName());
            }
            sendToClient(++i + ". Exit");

            chatId = mapper.readValue(input.readLine(), Long.class);
            if (i != chatId) {
                signInRoom();
            } else {
                closeClient();
            }
        } else {
            sendToClient("There are no chat rooms available. Please create a new one.");
            createRoom();
        }

    }

    private void signInRoom() throws IOException {
        sendToClient("Enter password for chat:");
        String password = mapper.readValue(input.readLine(), String.class);
        if (chatroomService.signIn(chatId, password)) {
            loadChatroom();
            startMessaging();
        } else {
            sendToClient("Incorrect password!");
            closeConnection();
        }
    }

    private void loadChatroom() {
        List<Message> messages = messageService.loadMessages(chatId);
        String chatName = chatroomService.getNameById(chatId);
        sendToClient(chatName + " ---");
        for (Message message : messages) {
            String username = usersService.getUsername(message.getSenderId());
            sendToClient(username + ": " + message.getText());
        }
    }

    private void startMessaging() {
        Thread readerThread = new Thread(() -> {
            try {
                String message;
                while (true) {
                    message = mapper.readValue(input.readLine(), String.class);
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    Server.sendMessageToChats(chatId, username, message);
                    saveMessage(message, chatId);
                }
                closeClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        readerThread.start();
    }

    public void sendMessage(String name, String text) {
        sendToClient(name + ": " + text);
    }

    private void saveMessage(String text, Long chatId) {
        Message message = new Message();
        message.setSenderId(userId);
        message.setChatId(chatId);
        message.setText(text);
        message.setTime(LocalDateTime.now());
        messageService.save(message);
    }

    private void closeConnection() {
        try {
            socket.close();
            input.close();
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
