package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component("chatroomRepository")
public class ChatroomRepositoryImpl implements ChatroomRepository {
    private JdbcTemplate template;

    @Autowired
    public ChatroomRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Chatroom> findById(Long id) {
        List<Chatroom> chatrooms = template.query(
                "select * from service.chatrooms where id = ?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Chatroom.class));
        return chatrooms.isEmpty() ? Optional.empty() : Optional.ofNullable(chatrooms.get(0));
    }

    @Override
    public List<Chatroom> findAll() {
        List<Chatroom> chatrooms = template.query(
                "select * from service.chatrooms",
                new BeanPropertyRowMapper<>(Chatroom.class));
        return chatrooms;
    }

    @Override
    public void save(Chatroom chatroom) {
        int result = template.update(
                "insert into service.chatrooms(name, password, owner_id) values (?, ?, ?)",
                chatroom.getName(), chatroom.getPassword(), chatroom.getOwnerId());

        if (result == 0) {
            System.err.println("Chatroom hasn't saved");
        }
    }

    @Override
    public void update(Chatroom chatroom) {
        int result = template.update(
                "update service.chatrooms set name = ?, password = ?, owner_id = ?  where id = ?",
                chatroom.getName(), chatroom.getOwnerId(), chatroom.getPassword());

        if (result == 0) {
            System.err.println("Chatroom hasn't updated");
        }
    }

    @Override
    public void delete(Long id) {
        int result = template.update(
                "delete from service.chatrooms where id = ?", id);

        if (result == 0) {
            System.err.println("Chatroom hasn't deleted");
        }
    }

    @Override
    public Optional<Chatroom> findByName(String name) {
        List<Chatroom> chatrooms = template.query(
                "select * from service.chatrooms where name = ?",
                new Object[]{name},
                new BeanPropertyRowMapper<>(Chatroom.class));
        return chatrooms.isEmpty() ? Optional.empty() : Optional.ofNullable(chatrooms.get(0));
    }
}
