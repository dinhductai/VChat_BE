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

    public static final String GET_ALL_STORY_PHOTOS =
            "SELECT \n" +
                    "    u.user_id as userId ,\n" +
                    "    up.full_name as fullName ,\n" +
                    "    (SELECT p.photo_url \n" +
                    "     FROM photos p \n" +
                    "     WHERE p.user_id = u.user_id \n" +
                    "     AND p.is_profile_picture = TRUE \n" +
                    "     ORDER BY p.upload_date DESC \n" +
                    "     LIMIT 1) as profileUrl,\n" +
                    "    GROUP_CONCAT(p2.photo_url ORDER BY p2.upload_date DESC SEPARATOR ',') as listStoryPhoto,\n" +
                    "    GROUP_CONCAT(p2.upload_date ORDER BY p2.upload_date DESC SEPARATOR ',') as listDateUpload\n" +
                    "FROM \n" +
                    "    users u\n" +
                    "    INNER JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "    LEFT JOIN photos p2 ON u.user_id = p2.user_id \n" +
                    "        AND p2.is_story = TRUE \n" +
                    "        AND p2.upload_date >= NOW() - INTERVAL 1 DAY\n" +
                    "    INNER JOIN matches m ON (\n" +
                    "        (m.sender_id = u.user_id AND m.receiver_id = :userId) \n" +
                    "        OR \n" +
                    "        (m.receiver_id = u.user_id AND m.sender_id = :userId)\n" +
                    "    ) AND m.status = 'MATCHED'\n" +
                    "WHERE \n" +
                    "    u.account_status = 'ACTIVE'\n" +
                    "    AND u.user_id != :userId\n" +
                    "GROUP BY \n" +
                    "    u.user_id, up.full_name\n" +
                    "HAVING \n" +
                    "    listStoryPhoto IS NOT NULL ";

    public static  final String GET_OWNER_STORIES =
            "SELECT\n" +
                    "    u.user_id as userId,\n" +
                    "    up.full_name as fullName,\n" +
                    "    (SELECT p.photo_url\n" +
                    "     FROM photos p\n" +
                    "     WHERE p.user_id = u.user_id\n" +
                    "       AND p.is_profile_picture = TRUE\n" +
                    "     ORDER BY p.upload_date DESC\n" +
                    "     LIMIT 1) as profileUrl,\n" +
                    "    GROUP_CONCAT(p2.photo_url ORDER BY p2.upload_date DESC SEPARATOR ',') as listStoryPhoto,\n" +
                    "    GROUP_CONCAT(p2.upload_date ORDER BY p2.upload_date DESC SEPARATOR ',') as listDateUpload\n" +
                    "FROM\n" +
                    "    users u\n" +
                    "    INNER JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "    LEFT JOIN photos p2 ON u.user_id = p2.user_id\n" +
                    "        AND p2.is_story = TRUE\n" +
                    "WHERE\n" +
                    "    u.user_id = :userId\n" +
                    "    AND u.account_status = 'ACTIVE'\n" +
                    "GROUP BY\n" +
                    "    u.user_id, up.full_name\n" +
                    "HAVING\n" +
                    "    listStoryPhoto IS NOT NULL;";
}
