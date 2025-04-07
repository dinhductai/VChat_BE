package com.website.loveconnect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Table(name = "token_id")
@Entity
@Getter
@Setter
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
