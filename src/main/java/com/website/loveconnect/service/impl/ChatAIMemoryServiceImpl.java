package com.website.loveconnect.service.impl;

import com.website.loveconnect.entity.ChatAIMemory;
import com.website.loveconnect.repository.ChatAIMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatAIMemoryServiceImpl implements ChatMemory {

    private final ChatAIMemoryRepository chatAIMemoryRepository;

    @Override
    public void add(String conversationId, List<Message> messages) {
        Integer currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new IllegalStateException("User not authenticated, cannot save chat memory.");
        }

        for (Message message : messages) {
            ChatAIMemory entity = new ChatAIMemory();
            entity.setUserId(currentUserId);
            entity.setConversationId(conversationId);
            entity.setRole(message.getMessageType().getValue().toUpperCase());
            entity.setContent(message.getText());
            chatAIMemoryRepository.save(entity);
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        Integer currentUserId = getCurrentUserId();
        List<ChatAIMemory> history = chatAIMemoryRepository
                .findByConversationIdOrderByCreateAtAsc(conversationId);

        return history.stream().map(mem -> {
            if ("USER".equalsIgnoreCase(mem.getRole())) {
                return new UserMessage(mem.getContent());
            } else {
                return new AssistantMessage(mem.getContent());
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        Integer currentUserId = getCurrentUserId();
        chatAIMemoryRepository.deleteByConversationId(conversationId);
    }

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return Integer.parseInt(jwt.getSubject());
        }
        return null;
    }
}