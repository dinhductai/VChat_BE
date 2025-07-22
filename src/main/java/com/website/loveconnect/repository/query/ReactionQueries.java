package com.website.loveconnect.repository.query;

public class ReactionQueries {
    public static final String COUNT_REACTION_ON_A_POST =
            "SELECT COUNT(*) AS total_reactions\n" +
                    "FROM reactions\n" +
                    "WHERE content_id = :postId AND content_type = 'POST';";

    public static final String COUNT_REACTION_AND_CHECK_USER_REACT =
            "SELECT\n" +
                    "    (SELECT COUNT(*) \n" +
                    "     FROM reactions \n" +
                    "     WHERE content_id = :postId AND content_type = 'POST'\n" +
                    "    ) AS totalReaction,\n" +
                    "\n" +
                    "    EXISTS (\n" +
                    "        SELECT 1 \n" +
                    "        FROM reactions \n" +
                    "        WHERE content_id = :postId AND content_type = 'POST' AND user_id = :userId\n" +
                    "    ) AS isReacted,\n" +
                    "\n" +
                    "    (SELECT e.emotion_name\n" +
                    "     FROM reactions r\n" +
                    "     JOIN emotions e ON r.emotion_id = e.emotion_id\n" +
                    "     WHERE r.content_id = :postId AND r.content_type = 'POST' AND r.user_id = :userId\n" +
                    "    ) AS emotionName;";
}
