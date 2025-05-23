package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Message;
import com.website.loveconnect.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllBySenderAndReceiver(User sender, User receiver);


}
