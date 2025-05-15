package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Like;
import com.website.loveconnect.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsBySenderAndReceiver(User sender, User receiver);
    Like findBySenderAndReceiver(User sender, User receiver);
}
