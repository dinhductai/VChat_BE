package com.website.loveconnect.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "video")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Integer videoId;

    @Column(name = "video_url",nullable = false)
    private String videoUrl;

    @Column(name = "upload_date")
    private Timestamp uploadDate;

    @Column(name = "is_approved")
    private Boolean isApproved = Boolean.TRUE;

    @Column(name = "is_story")
    private Boolean isStory = Boolean.FALSE;

//    @Column(name = "is_reel")
//    private Boolean isReel = Boolean.FALSE;

    @Column(name = "review_date")
    private Timestamp reviewDate;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User ownedVideo;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PostVideo> videoPosts = new ArrayList<>();


}
