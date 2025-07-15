package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Table(name = "users")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "registration_date")
    private Timestamp registrationDate;

    @Column(name = "last_login_date")
    private Timestamp lastLoginDate;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserProfile> userProfiles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ownedPhoto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Photo> ownedPhotos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reviewedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Photo> reviewedPhotos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ownedVideo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Video> ownedVideos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reviewedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Video> reviewedVideos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserInterest> userInterests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserNotification> userNotifications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> createdReports = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reported",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> receivedReports = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reviewer",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> reviewedReports = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Match> createdMatch = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Match> receivedMatch = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Message> listMessageOfSender = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Message> listMessageOfReceiver = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserSubscription> listSubOfUser = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Like> likesOfSender = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Like> likesOfReceiver = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reviewedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> reviewedPosts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserPost> userPosts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> userComments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Reaction> userReactions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Notification> userNotification = new ArrayList<>();
}

