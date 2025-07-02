package com.website.loveconnect.repository.query;

public class PhotoQueries {
    public static final String FIND_OWNED_PHOTO =
            "SELECT p.photo_url " +
                    "From photos p " +
                    "Join users u on u.user_id = p.user_id " +
                    "where u.user_id = :idUser " +
                    "order by p.is_profile_picture desc, p.upload_date asc ";
    public static final String FIND_ONE_BY_USER_ID =
            "SELECT p.* FROM photos p WHERE p.user_id = :userId " +
            "AND p.is_profile_picture = 1 AND p.is_approved = 1 ORDER BY p.upload_date DESC " +
            "LIMIT 1 ";
}
