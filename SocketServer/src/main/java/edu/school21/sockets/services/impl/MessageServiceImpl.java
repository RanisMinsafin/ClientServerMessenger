package edu.school21.sockets.services.impl;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.MessageRepository;
import edu.school21.sockets.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageServiceImpl implements MessageService {
    private MessageRepository repository;

    @Autowired
    public MessageServiceImpl(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Message message) {
        repository.save(message);
    }

    @Override
    public List<Message> loadMessages(Long chatId) {
        return repository.loadMessages(chatId);
    }
}
