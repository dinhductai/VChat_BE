package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Post;
import com.website.loveconnect.repository.query.PostQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = PostQueries.GET_RANDOM_POST,nativeQuery = true)
    Page<Tuple> getRandomPost(Pageable pageable);

    @Query(value = PostQueries.GET_ONE_POST_BY_POST_ID,nativeQuery = true)
    Tuple getOnePostByPostId(@Param("postId") Integer postId);

    @Query(value = PostQueries.GET_POSTS_BY_USERID,nativeQuery = true)
    Page<Tuple> getPostsByUserId(@Param("userId") Integer userId,Pageable pageable);

    @Query(value = PostQueries.GET_ONE_REEL_BY_POST_ID,nativeQuery = true)
    Tuple getOneReelByPostId(@Param("postId") Integer postId);
}
