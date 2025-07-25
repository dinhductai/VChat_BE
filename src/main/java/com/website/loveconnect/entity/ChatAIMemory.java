package com.website.loveconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "chat_ai_memory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatAIMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer chatId;

    @Column(name = "conversation_id",nullable = false)
    private String conversationId;

    @Column(name = "role",nullable = false)
    private String role;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private Timestamp createAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "user_id",nullable = false)
    private Integer userId;
}
