package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.entity.Message;
import com.website.loveconnect.service.MessageService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMessageController {
    MessageService messageService;

    @GetMapping(value = "/message/{senderId}/{receiverId}")
    public ResponseEntity<ApiResponse<List<Message>>> getAllMessageBySenderIdAndReceiverId(
            @PathVariable("senderId") Integer senderId,
            @PathVariable("receiverId") Integer receiverId) {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all message by senderId and receiverId successful",
                messageService.getAllMessageBySenderIdAndReceiverId(senderId,receiverId)));
    }


}
