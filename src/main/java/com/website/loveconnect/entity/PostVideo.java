package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.entity.entityid.PostVideoId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_videos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(PostVideoId.class)
public class PostVideo {
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "video_id",nullable = false)
    private Video video;

}
