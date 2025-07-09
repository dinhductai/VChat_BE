package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.EmotionName;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emotions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Integer emotionId;

    @Column(name = "emotion_name")
    @Enumerated(EnumType.STRING)
    private EmotionName emotionName;

    @JsonIgnore
    @OneToMany(mappedBy = "emotion", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Reaction> listReaction = new ArrayList<>();

}
