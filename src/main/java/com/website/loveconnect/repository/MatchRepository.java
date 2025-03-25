package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Match;
import com.website.loveconnect.enumpackage.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findBySenderUserIdOrReceiverUserId(Integer senderId, Integer receiverId);
    List<Match> findByStatus(MatchStatus status);
}
