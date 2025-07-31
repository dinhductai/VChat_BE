package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Integer> {
    UserPost findByUserAndPostAndUpload(User user, Post post,Boolean upload);
    void deleteByUserAndPost(User user, Post post);
}
