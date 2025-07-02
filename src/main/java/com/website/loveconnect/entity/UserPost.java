package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.entity.entityid.UserPostId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(UserPostId.class)
public class UserPost {
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @Column(name = "upload")
    private Boolean upload = Boolean.FALSE;

    @Column(name = "share")
    private Boolean share = Boolean.FALSE;

    @Column(name = "save")
    private Boolean save = Boolean.FALSE;
}
