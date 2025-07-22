package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.ChatAIRequest;
import com.website.loveconnect.service.ChatAIService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatAIController {

    ChatAIService chatAIService;

    @PostMapping(value = "/open-ai")
    public String openAI(@RequestBody ChatAIRequest chatAIRequest) {
        return chatAIService.chatAI(chatAIRequest);
    }
}
