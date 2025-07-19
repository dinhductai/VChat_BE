package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.CommentResponse;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class CommentMapper {
    public CommentResponse toCommentResponse(Tuple tuple) {
        return CommentResponse.builder()
                .commentId(tuple.get("commentId",Integer.class))
                .postId(tuple.get("postId",Integer.class))
                .content(tuple.get("content",String.class))
                .isEdited(tuple.get("isEdited",Boolean.class))
                .isDeleted(tuple.get("isDeleted",Boolean.class))
                .parentCommentId(tuple.get("parentCommentId",Integer.class))
                .level(tuple.get("level",Integer.class))
                .userId(tuple.get("userId",Integer.class))
                .commentDate(tuple.get("commentDate", Timestamp.class))
                .fullName(tuple.get("fullName",String.class))
                .bio(tuple.get("bio",String.class))
                .phoneNumber(tuple.get("phoneNumber",String.class))
                .photoUrl(tuple.get("photoUrl",String.class))
                .build();
    }

    public CommentResponse tupleToCommentResponse(Tuple tuple) {
        if (tuple == null) {
            return null;
        }
        return CommentResponse.builder()
                .commentId(tuple.get("commentId", Integer.class))
                .postId(tuple.get("postId", Integer.class))
                .content(tuple.get("content", String.class))
                .isEdited(tuple.get("isEdited", Boolean.class))
                .isDeleted(tuple.get("isDeleted", Boolean.class))
                .parentCommentId(tuple.get("parentCommentId", Integer.class) )
                .level(tuple.get("level", Integer.class))
                .userId(tuple.get("userId", Integer.class))
                .commentDate(tuple.get("commentDate", Timestamp.class))
                .fullName(tuple.get("fullName", String.class))
                .bio(tuple.get("bio", String.class))
                .phoneNumber(tuple.get("phoneNumber", String.class))
                .photoUrl(tuple.get("photoUrl", String.class))
                .build();
    }
}
