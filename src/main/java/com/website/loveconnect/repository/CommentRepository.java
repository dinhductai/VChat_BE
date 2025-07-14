package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Comment;
import com.website.loveconnect.repository.query.CommentQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = CommentQueries.GET_COMMENTS, nativeQuery = true)
    Page<Tuple> getComments(Pageable pageable,@Param("postId") Integer postId,@Param("level") Integer level,
                            @Param("parentCommentId") Integer parentCommentId) ;

    List<Comment> findByPostPostIdOrderByCommentDateAsc(Integer postId);


    @Query(value = CommentQueries.GET_ALL_COMMENTS_FOR_TREE, nativeQuery = true)
    List<Tuple> getAllCommentsByPostId(@Param("postId") Integer postId);


}
