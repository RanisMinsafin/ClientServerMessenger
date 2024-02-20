package edu.school21.sockets.services.impl;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repositories.ChatroomRepository;
import edu.school21.sockets.services.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ChatroomServiceImpl implements ChatroomService {
    private ChatroomRepository repository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ChatroomServiceImpl(ChatroomRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(Chatroom chatroom) {
        chatroom.setPassword(passwordEncoder.encode(chatroom.getPassword()));
        repository.save(chatroom);
    }

    @Override
    public boolean signIn(Long id, String password) {
        Optional<Chatroom> optionalChatroom = repository.findById(id);

        if (optionalChatroom.isPresent()) {
            Chatroom expected = optionalChatroom.get();
            return passwordEncoder.matches(password, expected.getPassword());
        }

        return false;
    }

    @Override
    public Optional<Chatroom> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public String getNameById(Long id) {
        Optional<Chatroom> chatroom = repository.findById(id);
        if (chatroom.isPresent()) {
            return chatroom.get().getName();
        }
        return "";
    }

    @Override
    public List<Chatroom> findAll() {
        return repository.findAll();
    }
}
