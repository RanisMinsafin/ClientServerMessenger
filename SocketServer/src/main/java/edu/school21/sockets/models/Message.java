package edu.school21.sockets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Component
public class Message {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String text;
    private LocalDateTime time;

    public Message() {}
}
