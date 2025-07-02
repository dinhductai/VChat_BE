package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.entity.entityid.PostVideoId;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "post_photos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(PostPhoto.class)
public class PostPhoto implements Serializable {
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "photo_id",nullable = false)
    private Photo photo;

}
