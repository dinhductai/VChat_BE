package com.website.loveconnect.repository;

import com.website.loveconnect.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUser_UserId(Integer userId);
    Optional<UserProfile> findByFullName(String username);
    Optional<UserProfile> findFullNameByUser_UserId(Integer userId);
}
