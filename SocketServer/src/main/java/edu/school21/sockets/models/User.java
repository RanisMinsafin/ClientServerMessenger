package edu.school21.sockets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class User {
    private Long id;
    private String username;
    private String password;

    public User() {
    }
}
