package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessageService {
    void save(Message message);
    List<Message> loadMessages(Long chatId);
}
