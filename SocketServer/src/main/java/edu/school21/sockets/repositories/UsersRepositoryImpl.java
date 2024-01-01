package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component("usersRepository")
public class UsersRepositoryImpl implements UsersRepository {
    private JdbcTemplate template;

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        createTables();
    }

    private void createTables() {
        template.execute("drop schema if exists service cascade;\n" +
                "create schema if not exists service;\n" +
                "create table service.users\n" +
                "(\n" +
                "    id       serial primary key,\n" +
                "    username varchar(255) unique,\n" +
                "    password varchar(255)\n" +
                ");");

        template.execute(
                "create schema if not exists service;\n" +
                        "create table if not exists service.chatrooms (\n" +
                        "    id serial primary key,\n" +
                        "    name varchar(255) unique,\n" +
                        "    password varchar(255),\n" +
                        "    owner_id int not null,\n" +
                        "    foreign key (owner_id) references service.users(id)\n" +
                        ");\n");

        template.execute(
                "create schema if not exists service;\n" +
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
    public Optional<User> findById(Long id) {
        List<User> users = template.query(
                "select * from service.users where id = ?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(User.class));
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    @Override
    public List<User> findAll() {
        List<User> users = template.query(
                "select * from service.users",
                new BeanPropertyRowMapper<>(User.class));
        return users;
    }

    @Override
    public void save(User user) {
        int result = template.update(
                "insert into service.users(username, password) values (?, ?)",
                user.getUsername(), user.getPassword());

        if (result == 0) {
            System.err.println("User hasn't saved");
        }
    }

    @Override
    public void update(User user) {
        int result = template.update(
                "update service.users set username = ?, password = ? where id = ?",
                user.getUsername(), user.getPassword(), user.getId());

        if (result == 0) {
            System.err.println("User hasn't updated");
        }
    }

    @Override
    public void delete(Long id) {
        int result = template.update(
                "delete from service.users where id = ?", id);

        if (result == 0) {
            System.err.println("User hasn't deleted");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = template.query(
                "select * from service.users where username = ?",
                new Object[]{username},
                new BeanPropertyRowMapper<>(User.class));
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }
}
