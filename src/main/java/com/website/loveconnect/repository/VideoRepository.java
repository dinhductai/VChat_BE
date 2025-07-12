package com.website.loveconnect.repository;

import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    Page<Video> findByOwnedVideoAndIsStory(User ownedVideo,Boolean isStory, Pageable pageable);
}
