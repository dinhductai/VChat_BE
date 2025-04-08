package com.website.loveconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "invalidated_token")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvalidatedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenId;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry_time")
    private Date expiryTime;
}
