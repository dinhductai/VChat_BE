package com.website.loveconnect.repository.query;

public class PostQueries {
    public static final String GET_RANDOM_POST =
            "SELECT \n" +
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
                    "LEFT JOIN photos prof_pic \n" +
                    "    ON prof_pic.user_id = u.user_id AND prof_pic.is_profile_picture = TRUE\n" +
                    "LEFT JOIN post_photos pp ON p.post_id = pp.post_id\n" +
                    "LEFT JOIN photos ph ON pp.photo_id = ph.photo_id\n" +
                    "LEFT JOIN post_videos pv ON p.post_id = pv.post_id\n" +
                    "LEFT JOIN video v ON pv.video_id = v.video_id\n" +
                    "WHERE p.is_public = TRUE\n" +
                    "\n" +
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
                    "ORDER BY RAND()";
}
