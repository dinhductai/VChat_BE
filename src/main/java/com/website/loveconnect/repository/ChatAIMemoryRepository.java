package com.website.loveconnect.repository;

import com.website.loveconnect.entity.ChatAIMemory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatAIMemoryRepository extends JpaRepository<ChatAIMemory, Integer> {
    List<ChatAIMemory> findByConversationIdOrderByCreateAtAsc(String conversationId);
    void deleteByConversationId(String conversationId);
}
