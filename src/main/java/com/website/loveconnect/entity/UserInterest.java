package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_interests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInterest  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_interests_id")
    private Integer userInterestId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "interest_id", nullable = false)
    private Interest interest;


}
