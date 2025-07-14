package com.website.loveconnect.repository.query;

public class ReactionQueries {
    public static final String COUNT_REACTION_ON_A_POST =
            "SELECT COUNT(*) AS total_reactions\n" +
                    "FROM reactions\n" +
                    "WHERE content_id = :postId AND content_type = 'POST';";
}
