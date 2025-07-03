package com.website.loveconnect.repository;

import com.website.loveconnect.entity.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Integer> {
}
