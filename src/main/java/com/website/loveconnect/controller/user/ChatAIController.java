package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.ChatAIRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.ChatAIConversationResponse;
import com.website.loveconnect.dto.response.ChatAIResponse;
import com.website.loveconnect.service.ChatAIService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatAIController {

    ChatAIService chatAIService;
    @Operation(summary = "Chat with AI", description = "Chat with text only, image only, or both")
    @PostMapping(value = "/open-ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ChatAIConversationResponse>> chat(
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "conversationId", required = false) String conversationId,
            @AuthenticationPrincipal Jwt jwt) {
        Integer userId  = Integer.parseInt(jwt.getSubject());
        if (conversationId == null || conversationId.isBlank()) {
            conversationId = UUID.randomUUID().toString();
        }
        List<ChatAIResponse> chatAIResponses = chatAIService.chat(message, file,conversationId,userId);
        ChatAIConversationResponse response = new ChatAIConversationResponse(conversationId,chatAIResponses);
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successful", response));
    }
}
