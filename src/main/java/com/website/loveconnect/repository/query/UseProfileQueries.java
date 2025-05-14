package com.website.loveconnect.repository.query;

public class UseProfileQueries {
    public static final String FIND_FULL_NAME_AND_PROFILE_PHOTOS_BY_USER_ID =
            "SELECT up.full_name, p.photo_url\n" +
                    "FROM user_profiles up\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT user_id, photo_url\n" +
                    "    FROM photos\n" +
                    "    WHERE is_profile_picture = true AND is_approved = true\n" +
                    "    ORDER BY upload_date DESC\n" +
                    "    LIMIT 1\n" +
                    ") p ON p.user_id = up.user_id\n" +
                    "WHERE up.user_id = :userId;";
}
