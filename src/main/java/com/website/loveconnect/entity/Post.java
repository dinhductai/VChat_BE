package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "posts")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "content")
    private String content;

    @Column(name = "upload_date")
    private Timestamp uploadDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "is_approved")
    private Boolean isApproved = Boolean.TRUE;

    @Column(name = "reviewed_date")
    private Timestamp reviewedDate;

    @Column(name = "is_public")
    private Boolean isPublic = Boolean.TRUE;

    @Column(name = "is_reel")
    private Boolean isReel = Boolean.FALSE;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.ACTIVE;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PostVideo> postVideos = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PostPhoto> postPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserPost> postUsers = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}
