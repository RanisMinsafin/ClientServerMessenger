package edu.school21.sockets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@ToString
@Component
public class Chatroom {
    private Long id;
    private Long ownerId;
    private String name;
    private String password;

    public Chatroom() {
    }
}
