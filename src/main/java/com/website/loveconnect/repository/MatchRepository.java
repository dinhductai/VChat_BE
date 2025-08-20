package com.website.loveconnect.repository;

import com.website.loveconnect.dto.response.MatchStatusResponse;
import com.website.loveconnect.entity.Match;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.repository.query.MatchQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findBySenderUserIdOrReceiverUserId(int senderId, int receiverId);
    List<Match> findByStatus(MatchStatus status);
    List<Match> findBySenderUserId(int userId);

    @Query(value = MatchQueries.GET_ALL_MATCH_BY_SENDER_ID,nativeQuery = true)
    List<Tuple> getMatchesBySenderId(@Param("senderId") int senderId, Pageable pageable);

    @Query(value = MatchQueries.GET_MATCH_BY_MATCH_ID,nativeQuery = true)
    Optional<Tuple> getMatchByMatchId(@Param("matchId") int matchId);

    @Query(value = MatchQueries.GET_FULL_NAME_AND_PROFILE_USER_MATCHED_BY_SENDER_ID,nativeQuery = true)
    List<Tuple> getFullNameAndProfileUserMatchedBySenderId(@Param("senderId") int senderId, Pageable pageable);

    Optional<Match> findBySenderAndReceiver(User sender, User receiver);

    Match findBySenderAndReceiverAndStatus(User sender,User receiver, MatchStatus status);

    @Query(value = MatchQueries.CHECK_MATCHED_STATUS_BY_USER_ID_AND_OTHER_ID,nativeQuery = true)
    Integer checkMatchStatus(@Param("userId") Integer userId, @Param("otherUserId") Integer otherUserId);

    @Query(value = MatchQueries.GET_MATCHED_ID,nativeQuery = true)
    Integer getMatchedId(@Param("senderId") Integer senderId,@Param("receiverId") Integer receiverId);

}
