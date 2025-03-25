package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "photos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @Column(name = "photo_url",nullable = false)
    private String photoUrl;

    @Column(name = "is_profile_picture")
    private Boolean isProfilePicture;

    @Column(name = "upload_date")
    private Timestamp uploadDate;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "review_date")
    private Timestamp reviewDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User ownedPhoto;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;



}
