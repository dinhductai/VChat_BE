package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Message;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.repository.query.MessageQueries;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllBySenderAndReceiver(User sender, User receiver);
    @Query(value = MessageQueries.GET_MESSAGES_BY_SENDER_AND_RECEIVER_ID, nativeQuery = true)
    Page<Tuple> findAllMessageBySenderIdAndReceiverId(@Param("senderId") Integer senderId ,
                                                      @Param("receiverId") Integer receiverId,
                                                      Pageable pageable);

}
