package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;

import java.util.List;
import java.util.Optional;

public interface ChatroomService {
    void save(Chatroom chatroom);

    boolean signIn(Long id, String password);

    Optional<Chatroom> findByName(String name);

    String getNameById(Long id);

    List<Chatroom> findAll();
}
