package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Table(name = "user_profiles")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(name = "birthdate",nullable = false)
    private Date birthDate;

    @Column(name = "gender",nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "looking_for",nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender lookingFor;

    @Column(name = "bio")
    private String bio;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "location")
    private String location;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "company")
    private String company;

    @Column(name = "education")
    private String education;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_complete")
    private Boolean profileComplete;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

}
