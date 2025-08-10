package com.website.loveconnect.repository.query;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentQueries {
    public static final String GET_COMMENTS = "SELECT\n" +
            "    c.comment_id AS commentId,\n" +
            "    c.post_id AS postId,\n" +
            "    c.content AS content,\n" +
            "    c.comment_date AS commentDate,\n" +
            "    c.is_edited AS isEdited,\n" +
            "    c.is_deleted AS isDeleted,\n" +
            "    c.parent_comment_id AS parentCommentId,\n" +
            "    c.level AS level,\n" +
            "    c.user_id AS userId,\n" +
            "    up.full_name AS fullName,\n" +
            "    up.bio AS bio,\n" +
            "    u.phone_number AS phoneNumber,\n" +
            "    prof_pic.photo_url AS photoUrl\n" +
            "FROM comments c\n" +
            "JOIN users u \n" +
            "    ON c.user_id = u.user_id\n" +
            "JOIN user_profiles up \n" +
            "    ON u.user_id = up.user_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT p.user_id, p.photo_url\n" +
            "    FROM photos p\n" +
            "    INNER JOIN (\n" +
            "        SELECT user_id, MAX(upload_date) AS max_upload\n" +
            "        FROM photos\n" +
            "        WHERE is_profile_picture = TRUE\n" +
            "        GROUP BY user_id\n" +
            "    ) latest \n" +
            "        ON p.user_id = latest.user_id \n" +
            "        AND p.upload_date = latest.max_upload\n" +
            ") AS prof_pic\n" +
            "    ON c.user_id = prof_pic.user_id\n" +
            "WHERE\n" +
            "    c.post_id = :postId\n" +
            "    AND c.level = :level\n" +
            "    AND (:parentCommentId IS NULL OR c.parent_comment_id = :parentCommentId);\n";


    public static final String GET_ALL_COMMENTS_FOR_TREE = "SELECT\n" +
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
            "ORDER BY c.comment_date ASC;";



}
