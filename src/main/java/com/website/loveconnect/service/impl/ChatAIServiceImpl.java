package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.ChatAIRequest;
import com.website.loveconnect.service.ChatAIService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ChatAIServiceImpl implements ChatAIService {
    final ChatClient chatClient;

    public  ChatAIServiceImpl(ChatClient.Builder builder){
        chatClient = builder.build();
    }


    @Override
    public String chatAI(ChatAIRequest chatAIRequest) {
        return chatClient.prompt(chatAIRequest.message()).call().content();
    }
}
