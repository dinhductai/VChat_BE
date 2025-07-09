package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Emotion;
import com.website.loveconnect.enumpackage.EmotionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotion, Integer> {
    Optional<Emotion> findByEmotionName(EmotionName emotionName);
}
