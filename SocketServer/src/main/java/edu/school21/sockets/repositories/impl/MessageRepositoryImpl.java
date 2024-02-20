package edu.school21.sockets.repositories.impl;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DependsOn({"usersRepository", "chatroomRepository"})
@Component("messageRepository")
public class MessageRepositoryImpl implements MessageRepository {
    private JdbcTemplate template;

    @Autowired
    public MessageRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        createTable();
    }

    private void createTable() {
        template.execute(
                "create table if not exists service.messages (\n" +
                        "    id serial primary key,\n" +
                        "    chat_id int not null,\n" +
                        "    sender_id int not null,\n" +
                        "    text text,\n" +
                        "    time timestamp,\n" +
                        "    foreign key (sender_id) references service.users(id),\n" +
                        "    foreign key (chat_id) references service.chatrooms(id)\n" +
                        ");\n");
    }


    @Override
    public Optional<Message> findById(Long id) {
        List<Message> messages = template.query(
                "select * from service.messages where id = ?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Message.class));
        return messages.isEmpty() ? Optional.empty() : Optional.ofNullable(messages.get(0));
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = template.query(
                "select * from service.messages",
                new BeanPropertyRowMapper<>(Message.class));
        return messages;
    }

    @Override
    public void save(Message message) {
        int result = template.update(
                "insert into service.messages(chat_id, sender_id, text, time) values (? ,?, ?, ?)",
                message.getChatId(), message.getSenderId(), message.getText(), Timestamp.valueOf(message.getTime()));

        if (result == 0) {
            System.err.println("Message hasn't saved");
        }
    }

    @Override
    public void update(Message message) {
        int result = template.update(
                "update service.messages set chat_id = ?, sender_id = ?, message_text = ?, time = ? where id = ?",
                message.getChatId(), message.getSenderId(), message.getText(),
                Timestamp.valueOf(message.getTime()), message.getId());

        if (result == 0) {
            System.err.println("Message hasn't updated");
        }
    }

    @Override
    public void delete(Long id) {
        int result = template.update(
                "delete from service.messages where id = ?", id);

        if (result == 0) {
            System.err.println("Message hasn't deleted");
        }
    }

    @Override
    public List<Message> loadMessages(Long chatId) {
        List<Message> messages = template.query(
                "select * from service.messages where chat_id = ? order by time desc limit 30",
                new Object[]{chatId},
                new BeanPropertyRowMapper<>(Message.class));

        Collections.reverse(messages);
        return messages;
    }
}
