package com.website.loveconnect.repository;

import com.website.loveconnect.dto.response.ReactionResponse;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.Reaction;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.ContentType;
import com.website.loveconnect.repository.query.ReactionQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    @Query(value = ReactionQueries.COUNT_REACTION_ON_A_POST,nativeQuery = true)
    Long countReactionOnAPost(@Param("postId") Integer postId);

    @Query(value = ReactionQueries.COUNT_REACTION_AND_CHECK_USER_REACT,nativeQuery = true)
    Optional<Tuple> countReactionAndCheckUserReaction(@Param("postId") Integer postId, @Param("userId") Integer userId);

    Reaction findByUserAndContentIdAndContentType(User user, Integer contentId, ContentType contentType);
}
