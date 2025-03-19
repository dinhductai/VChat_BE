package com.website.loveconnect.repository;

import com.website.loveconnect.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByUser_UserId(Integer userId);
}
