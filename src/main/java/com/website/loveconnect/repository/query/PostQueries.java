package com.website.loveconnect.repository.query;

public class PostQueries {
    public static final String GET_RANDOM_POST =
                    "SELECT\n" +
                            "    u.user_id as userId,\n" +
                            "    up.full_name as fullName,\n" +
                            "    up.bio as bio,\n" +
                            "    u.phone_number as phoneNumber,\n" +
                            "    prof_pic.photo_url AS profilePicture,\n" +
                            "    p.post_id as postId,\n" +
                            "    p.content as content,\n" +
                            "    p.upload_date as uploadDate,\n" +
                            "    p.status as status,\n" +
                            "    p.is_public as isPublic,\n" +
                            "    GROUP_CONCAT(DISTINCT ph.photo_url SEPARATOR ', ') AS photosUrl,\n" +
                            "    GROUP_CONCAT(DISTINCT v.video_url SEPARATOR ', ') AS videosUrl\n" +
                            "FROM posts p\n" +
                            "JOIN user_posts upo ON p.post_id = upo.post_id\n" +
                            "JOIN users u ON upo.user_id = u.user_id\n" +
                            "JOIN user_profiles up ON u.user_id = up.user_id\n" +
                            "LEFT JOIN (\n" +
                            "    SELECT\n" +
                            "        p.user_id,\n" +
                            "        p.photo_url\n" +
                            "    FROM photos AS p\n" +
                            "    INNER JOIN (\n" +
                            "        SELECT\n" +
                            "            user_id,\n" +
                            "            MAX(upload_date) AS latest_date\n" +
                            "        FROM photos\n" +
                            "        WHERE is_profile_picture = TRUE\n" +
                            "        GROUP BY user_id\n" +
                            "    ) AS latest_pic_dates \n" +
                            "    ON p.user_id = latest_pic_dates.user_id AND p.upload_date = latest_pic_dates.latest_date\n" +
                            "    WHERE p.is_profile_picture = TRUE \n" +
                            "\n" +
                            ") AS prof_pic ON u.user_id = prof_pic.user_id\n" +
                            "LEFT JOIN post_photos pp ON p.post_id = pp.post_id\n" +
                            "LEFT JOIN photos ph ON pp.photo_id = ph.photo_id\n" +
                            "LEFT JOIN post_videos pv ON p.post_id = pv.post_id\n" +
                            "LEFT JOIN video v ON pv.video_id = v.video_id\n" +
                            "WHERE p.is_public = TRUE AND p.is_approved = TRUE AND upo.upload = TRUE\n" +
                            "GROUP BY\n" +
                            "    p.post_id,\n" +
                            "    u.user_id,\n" +
                            "    up.full_name,\n" +
                            "    up.bio,\n" +
                            "    u.phone_number,\n" +
                            "    prof_pic.photo_url,\n" +
                            "    p.content,\n" +
                            "    p.upload_date,\n" +
                            "    p.status,\n" +
                            "    p.is_public\n" +
                            "ORDER BY RAND();";

    public static final String GET_ONE_POST_BY_POST_ID =
            "SELECT\n" +
                    "    DISTINCT u.user_id as userId,\n" +
                    "    up.full_name as fullName,\n" +
                    "    up.bio as bio,\n" +
                    "    u.phone_number as phoneNumber,\n" +
                    "    prof_pic.photo_url AS profilePicture,\n" +
                    "    p.post_id as postId,\n" +
                    "    p.content as content,\n" +
                    "    p.upload_date as uploadDate,\n" +
                    "    p.status as status,\n" +
                    "    p.is_public as isPublic,\n" +
                    "    GROUP_CONCAT(DISTINCT ph.photo_url SEPARATOR ', ') AS photosUrl,\n" +
                    "    GROUP_CONCAT(DISTINCT v.video_url SEPARATOR ', ') AS videosUrl\n" +
                    "FROM\n" +
                    "    posts p\n" +
                    "JOIN\n" +
                    "    user_posts upo ON p.post_id = upo.post_id\n" +
                    "JOIN\n" +
                    "    users u ON upo.user_id = u.user_id\n" +
                    "JOIN\n" +
                    "    user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        p.user_id,\n" +
                    "        p.photo_url\n" +
                    "    FROM photos AS p\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT\n" +
                    "            user_id,\n" +
                    "            MAX(upload_date) AS latest_date\n" +
                    "        FROM photos\n" +
                    "        WHERE is_profile_picture = TRUE\n" +
                    "        GROUP BY user_id\n" +
                    "    ) AS latest_pic_dates\n" +
                    "        ON p.user_id = latest_pic_dates.user_id AND p.upload_date = latest_pic_dates.latest_date\n" +
                    "    WHERE p.is_profile_picture = TRUE\n" +
                    "\n" +
                    ") AS prof_pic\n" +
                    "    ON u.user_id = prof_pic.user_id\n" +
                    "LEFT JOIN\n" +
                    "    post_photos pp ON p.post_id = pp.post_id\n" +
                    "LEFT JOIN\n" +
                    "    photos ph ON pp.photo_id = ph.photo_id\n" +
                    "LEFT JOIN\n" +
                    "    post_videos pv ON p.post_id = pv.post_id\n" +
                    "LEFT JOIN\n" +
                    "    video v ON pv.video_id = v.video_id\n" +
                    "WHERE\n" +
                    "    p.post_id = :postId\n" +
                    "GROUP BY\n" +
                    "    p.post_id,\n" +
                    "    u.user_id,\n" +
                    "    up.full_name,\n" +
                    "    up.bio,\n" +
                    "    u.phone_number,\n" +
                    "    prof_pic.photo_url,\n" +
                    "    p.content,\n" +
                    "    p.upload_date,\n" +
                    "    p.status,\n" +
                    "    p.is_public;";

    public static final String GET_POSTS_BY_USERID =
            "SELECT DISTINCT\n" +
                    "    u.user_id as userId,\n" +
                    "    up.full_name as fullName,\n" +
                    "    up.bio as bio,\n" +
                    "    u.phone_number as phoneNumber,\n" +
                    "    prof_pic.photo_url AS profilePicture,\n" +
                    "    p.post_id as postId,\n" +
                    "    p.content as content,\n" +
                    "    p.upload_date as uploadDate,\n" +
                    "    p.status as status,\n" +
                    "    p.is_public as isPublic,\n" +
                    "    GROUP_CONCAT(DISTINCT ph.photo_url SEPARATOR ', ') AS photosUrl,\n" +
                    "    GROUP_CONCAT(DISTINCT v.video_url SEPARATOR ', ') AS videosUrl\n" +
                    "FROM posts p\n" +
                    "JOIN user_posts upo ON p.post_id = upo.post_id\n" +
                    "JOIN users u ON upo.user_id = u.user_id\n" +
                    "JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        p.user_id,\n" +
                    "        p.photo_url\n" +
                    "    FROM photos AS p\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT\n" +
                    "            user_id,\n" +
                    "            MAX(upload_date) AS latest_date\n" +
                    "        FROM photos\n" +
                    "        WHERE is_profile_picture = TRUE\n" +
                    "        GROUP BY user_id\n" +
                    "    ) AS latest_pic_dates\n" +
                    "        ON p.user_id = latest_pic_dates.user_id AND p.upload_date = latest_pic_dates.latest_date\n" +
                    "    WHERE p.is_profile_picture = TRUE\n" +
                    "\n" +
                    ") AS prof_pic\n" +
                    "    ON u.user_id = prof_pic.user_id \n" +
                    "LEFT JOIN post_photos pp ON p.post_id = pp.post_id\n" +
                    "LEFT JOIN photos ph ON pp.photo_id = ph.photo_id\n" +
                    "LEFT JOIN post_videos pv ON p.post_id = pv.post_id\n" +
                    "LEFT JOIN video v ON pv.video_id = v.video_id\n" +
                    "WHERE u.user_id = :userId and p.is_reel =false and p.is_approved = true\n" +
                    "GROUP BY \n" +
                    "    p.post_id,\n" +
                    "    u.user_id,\n" +
                    "    up.full_name,\n" +
                    "    up.bio,\n" +
                    "    u.phone_number,\n" +
                    "    prof_pic.photo_url,\n" +
                    "    p.content,\n" +
                    "    p.upload_date,\n" +
                    "    p.status,\n" +
                    "    p.is_public\n" +
                    "ORDER BY p.upload_date DESC";


    public static final String GET_ONE_REEL_BY_POST_ID =
            "SELECT\n" +
                    "    u.user_id as userId,\n" +
                    "    up.full_name as fullName,\n" +
                    "    up.bio as bio,\n" +
                    "    u.phone_number as phoneNumber,\n" +
                    "    prof_pic.photo_url AS profilePicture,\n" +
                    "    p.post_id as postId,\n" +
                    "    p.content as content,\n" +
                    "    p.upload_date as uploadDate,\n" +
                    "    p.status as status,\n" +
                    "    p.is_public as isPublic,\n" +
                    "    v.video_url AS videosUrl\n" +
                    "FROM\n" +
                    "    posts p\n" +
                    "JOIN user_posts upo ON p.post_id = upo.post_id\n" +
                    "JOIN users u ON upo.user_id = u.user_id\n" +
                    "JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        user_id,\n" +
                    "        photo_url\n" +
                    "    FROM (\n" +
                    "        SELECT\n" +
                    "            user_id,\n" +
                    "            photo_url,\n" +
                    "            ROW_NUMBER() OVER(PARTITION BY user_id ORDER BY upload_date DESC) as rn\n" +
                    "        FROM photos\n" +
                    "        WHERE is_profile_picture = TRUE\n" +
                    "    ) AS ranked_photos\n" +
                    "    WHERE rn = 1\n" +
                    ") AS prof_pic\n" +
                    "    ON u.user_id = prof_pic.user_id\n" +
                    "\n" +
                    "LEFT JOIN post_photos pp ON p.post_id = pp.post_id\n" +
                    "LEFT JOIN photos ph ON pp.photo_id = ph.photo_id\n" +
                    "LEFT JOIN post_videos pv ON p.post_id = pv.post_id\n" +
                    "LEFT JOIN video v ON pv.video_id = v.video_id\n" +
                    "WHERE\n" +
                    "    p.post_id = :postId AND p.is_reel = TRUE\n" +
                    "GROUP BY\n" +
                    "    p.post_id,\n" +
                    "    u.user_id,\n" +
                    "    up.full_name,\n" +
                    "    up.bio,\n" +
                    "    u.phone_number,\n" +
                    "    prof_pic.photo_url,\n" +
                    "    p.content,\n" +
                    "    p.upload_date,\n" +
                    "    p.status,\n" +
                    "    p.is_public,\n" +
                    "    v.video_url; ";

    public static final String GET_RANDOM_REEL =
            "SELECT\n" +
                    "    u.user_id as userId,\n" +
                    "    up.full_name as fullName,\n" +
                    "    up.bio as bio,\n" +
                    "    u.phone_number as phoneNumber,\n" +
                    "    prof_pic.photo_url AS profilePicture,\n" +
                    "    p.post_id as postId,\n" +
                    "    p.content as content,\n" +
                    "    p.upload_date as uploadDate,\n" +
                    "    p.status as status,\n" +
                    "    p.is_public as isPublic,\n" +
                    "    v.video_url AS videosUrl\n" +
                    "FROM posts p\n" +
                    "JOIN user_posts upo ON p.post_id = upo.post_id\n" +
                    "JOIN users u ON upo.user_id = u.user_id\n" +
                    "JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "-- Sửa lại subquery lấy ảnh đại diện giống với query GET_RANDOM_POST\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        ph.user_id,\n" +
                    "        ph.photo_url\n" +
                    "    FROM photos AS ph\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT\n" +
                    "            user_id,\n" +
                    "            MAX(upload_date) AS latest_date\n" +
                    "        FROM photos\n" +
                    "        WHERE is_profile_picture = TRUE\n" +
                    "        GROUP BY user_id\n" +
                    "    ) AS latest_pic_dates\n" +
                    "    ON ph.user_id = latest_pic_dates.user_id AND ph.upload_date = latest_pic_dates.latest_date\n" +
                    "    WHERE ph.is_profile_picture = TRUE\n" +
                    ") AS prof_pic ON u.user_id = prof_pic.user_id\n" +
                    "LEFT JOIN post_videos pv ON p.post_id = pv.post_id\n" +
                    "LEFT JOIN video v ON pv.video_id = v.video_id\n" +
                    "WHERE p.is_public = TRUE AND p.is_reel = TRUE\n" +
                    "GROUP BY\n" +
                    "    p.post_id,\n" +
                    "    u.user_id,\n" +
                    "    up.full_name,\n" +
                    "    up.bio,\n" +
                    "    u.phone_number,\n" +
                    "    prof_pic.photo_url,\n" +
                    "    p.content,\n" +
                    "    p.upload_date,\n" +
                    "    p.status,\n" +
                    "    p.is_public,\n" +
                    "    v.video_url\n" +
                    "ORDER BY RAND();";


    public static final String GET_POST_SHARED_BY_USER_ID =
            "SELECT DISTINCT\n" +
                    "    u.user_id as userId,\n" +
                    "    up.full_name as fullName,\n" +
                    "    up.bio as bio,\n" +
                    "    u.phone_number as phoneNumber,\n" +
                    "    prof_pic.photo_url AS profilePicture,\n" +
                    "    p.post_id as postId,\n" +
                    "    p.content as content,\n" +
                    "    p.upload_date as uploadDate,\n" +
                    "    p.status as status,\n" +
                    "    p.is_public as isPublic,\n" +
                    "    GROUP_CONCAT(DISTINCT ph.photo_url SEPARATOR ', ') AS photosUrl,\n" +
                    "    GROUP_CONCAT(DISTINCT v.video_url SEPARATOR ', ') AS videosUrl\n" +
                    "FROM posts p\n" +
                    "JOIN user_posts upo ON p.post_id = upo.post_id\n" +
                    "JOIN users u ON upo.user_id = u.user_id\n" +
                    "JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        p.user_id,\n" +
                    "        p.photo_url\n" +
                    "    FROM photos AS p\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT\n" +
                    "            user_id,\n" +
                    "            MAX(upload_date) AS latest_date\n" +
                    "        FROM photos\n" +
                    "        WHERE is_profile_picture = TRUE\n" +
                    "        GROUP BY user_id\n" +
                    "    ) AS latest_pic_dates\n" +
                    "        ON p.user_id = latest_pic_dates.user_id AND p.upload_date = latest_pic_dates.latest_date\n" +
                    "    WHERE p.is_profile_picture = TRUE\n" +
                    ") AS prof_pic\n" +
                    "    ON u.user_id = prof_pic.user_id \n" +
                    "LEFT JOIN post_photos pp ON p.post_id = pp.post_id\n" +
                    "LEFT JOIN photos ph ON pp.photo_id = ph.photo_id\n" +
                    "LEFT JOIN post_videos pv ON p.post_id = pv.post_id\n" +
                    "LEFT JOIN video v ON pv.video_id = v.video_id\n" +
                    "WHERE u.user_id = :userId AND upo.share = TRUE AND p.is_reel = false AND p.is_approved = true\n" +
                    "GROUP BY \n" +
                    "    p.post_id,\n" +
                    "    u.user_id,\n" +
                    "    up.full_name,\n" +
                    "    up.bio,\n" +
                    "    u.phone_number,\n" +
                    "    prof_pic.photo_url,\n" +
                    "    p.content,\n" +
                    "    p.upload_date,\n" +
                    "    p.status,\n" +
                    "    p.is_public\n" +
                    "ORDER BY p.upload_date DESC;";

    public static final String GET_POST_SAVED_BY_USER_ID =
            "SELECT DISTINCT\n" +
                    "    u.user_id as userId,\n" +
                    "    up.full_name as fullName,\n" +
                    "    up.bio as bio,\n" +
                    "    u.phone_number as phoneNumber,\n" +
                    "    prof_pic.photo_url AS profilePicture,\n" +
                    "    p.post_id as postId,\n" +
                    "    p.content as content,\n" +
                    "    p.upload_date as uploadDate,\n" +
                    "    p.status as status,\n" +
                    "    p.is_public as isPublic,\n" +
                    "    GROUP_CONCAT(DISTINCT ph.photo_url SEPARATOR ', ') AS photosUrl,\n" +
                    "    GROUP_CONCAT(DISTINCT v.video_url SEPARATOR ', ') AS videosUrl\n" +
                    "FROM posts p\n" +
                    "JOIN user_posts upo ON p.post_id = upo.post_id\n" +
                    "JOIN users u ON upo.user_id = u.user_id\n" +
                    "JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        p.user_id,\n" +
                    "        p.photo_url\n" +
                    "    FROM photos AS p\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT\n" +
                    "            user_id,\n" +
                    "            MAX(upload_date) AS latest_date\n" +
                    "        FROM photos\n" +
                    "        WHERE is_profile_picture = TRUE\n" +
                    "        GROUP BY user_id\n" +
                    "    ) AS latest_pic_dates\n" +
                    "        ON p.user_id = latest_pic_dates.user_id AND p.upload_date = latest_pic_dates.latest_date\n" +
                    "    WHERE p.is_profile_picture = TRUE\n" +
                    ") AS prof_pic\n" +
                    "    ON u.user_id = prof_pic.user_id \n" +
                    "LEFT JOIN post_photos pp ON p.post_id = pp.post_id\n" +
                    "LEFT JOIN photos ph ON pp.photo_id = ph.photo_id\n" +
                    "LEFT JOIN post_videos pv ON p.post_id = pv.post_id\n" +
                    "LEFT JOIN video v ON pv.video_id = v.video_id\n" +
                    "WHERE u.user_id = :userId AND upo.save = TRUE AND p.is_reel = false AND p.is_approved = true\n" +
                    "GROUP BY \n" +
                    "    p.post_id,\n" +
                    "    u.user_id,\n" +
                    "    up.full_name,\n" +
                    "    up.bio,\n" +
                    "    u.phone_number,\n" +
                    "    prof_pic.photo_url,\n" +
                    "    p.content,\n" +
                    "    p.upload_date,\n" +
                    "    p.status,\n" +
                    "    p.is_public\n" +
                    "ORDER BY p.upload_date DESC;";
}
