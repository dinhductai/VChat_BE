package com.website.loveconnect.repository.query;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentQueries {
    public static final String GET_COMMENTS = "SELECT\n" +
            "    c.comment_id as commentId,\n" +
            "    c.post_id as postId,\n" +
            "    c.content as content,\n" +
            "    c.is_edited as isEdited,\n" +
            "    c.is_deleted as isDeleted,\n" +
            "    c.parent_comment_id as parentCommentId,\n" +
            "    c.level as level,\n" +
            "    c.user_id as userId,\n" +
            "    c.comment_date as commentDate,\n" +
            "    up.full_name as fullName,\n" +
            "    up.bio as bio,\n" +
            "    u.phone_number as phoneNumber,\n" +
            "    (SELECT p.photo_url\n" +
            "     FROM photos p \n" +
            "     WHERE p.user_id = c.user_id \n" +
            "     AND p.is_profile_picture = TRUE \n" +
            "     ORDER BY p.upload_date DESC \n" +
            "     LIMIT 1) AS photoUrl\n" +
            "FROM\n" +
            "    comments c\n" +
            "JOIN\n" +
            "    users u ON c.user_id = u.user_id\n" +
            "JOIN\n" +
            "    user_profiles up ON u.user_id = up.user_id\n" +
            "WHERE\n" +
            "    c.post_id = :postId\n" +
            "    AND c.level = :level\n" +
            "    AND (c.parent_comment_id = :parentCommentId OR :parentCommentId IS NULL);";
}
