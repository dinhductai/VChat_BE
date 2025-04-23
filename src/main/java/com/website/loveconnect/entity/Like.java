package com.website.loveconnect.entity;

import com.website.loveconnect.enumpackage.LikeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "likes")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer likeId;

    @Column(name = "like_date")
    private Timestamp likeDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus;

    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id",nullable = false)
    private User receiver;

}
