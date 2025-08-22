package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.MessageDeleteRequest;
import com.website.loveconnect.dto.request.MessageRequest;
import com.website.loveconnect.dto.request.MessageUpdateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.MessageResponse;
import com.website.loveconnect.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    MessageService messageService;
//    @PostMapping(value = "/send")
//    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest message) {
//        message.setSendAt(new Timestamp(System.currentTimeMillis()));
//        messagingTemplate.convertAndSend("/topic/messages", message);
//        return ResponseEntity.ok("Đã gửi!");
//    }

    @Operation(summary = "Get history chat",description = "Get all message of a topic chat")
    @GetMapping(value = "/message/history")
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getHistoryChat(@AuthenticationPrincipal Jwt jwt,
                                                                             @RequestParam("receiverId") Integer receiverId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size){
        Integer senderId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get history chat successful",
                messageService.getAllMessageBySenderIdAndReceiverId(senderId,receiverId,page,size)));
    }

    @PutMapping(value = "/message")
    public ResponseEntity<ApiResponse<MessageResponse>> updateMessage(@AuthenticationPrincipal Jwt jwt,
                                                                      @RequestBody MessageUpdateRequest messageRequest){
        Integer senderId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Update message successful",
                messageService.updateMessage(messageRequest,senderId)));
    }

    @DeleteMapping(value = "/message")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteMessage(@AuthenticationPrincipal Jwt jwt,
                                                                      MessageDeleteRequest messageDeleteRequest){
        Integer senderId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete message successful",
                messageService.deleteMessage(messageDeleteRequest,senderId)));
    }
}
