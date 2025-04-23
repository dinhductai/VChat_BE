package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Like;
import com.website.loveconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsBySenderAndReceiver(User sender, User receiver);
}
