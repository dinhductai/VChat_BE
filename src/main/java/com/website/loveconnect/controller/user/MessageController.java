package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.MessageRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static java.time.LocalTime.now;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate  messagingTemplate;

    @PostMapping(value = "/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest message) {
        message.setSendAt(new Timestamp(System.currentTimeMillis()));
        messagingTemplate.convertAndSend("/topic/messages", message);
        return ResponseEntity.ok("Đã gửi!");
    }

}
