package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Reaction;
import com.website.loveconnect.repository.query.ReactionQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    @Query(value = ReactionQueries.COUNT_REACTION_ON_A_POST,nativeQuery = true)
    Long countReactionOnAPost(@Param("postId") Integer postId);
}
