package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "messages")
@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "message_text",nullable = false)
    private String messageText;

    @Column(name = "sent_at")
    private Timestamp sentAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "match_id",nullable = false)
    private Match match;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "receiver_id",nullable = false)
    private User receiver;





}
