package com.website.loveconnect.repository.query;

public class NotificationQueries {
    public static final String GET_MATCH_NOTIFICATION_BY_USER_ID =
            "SELECT\n" +
                    "    n.notification_id AS notificationId,\n" +
                    "    n.notification_type AS notificationType,\n" +
                    "    n.content AS content,\n" +
                    "    n.created_at AS createAt,\n" +
                    "    n.sender_id AS senderId,\n" +
                    "    latest_photo.photo_url AS profilePicture\n" +
                    "FROM\n" +
                    "    user_notifications un\n" +
                    "JOIN\n" +
                    "    notifications n ON un.notification_id = n.notification_id\n" +
                    "-- SỬA ĐỔI QUAN TRỌNG BẮT ĐẦU TỪ ĐÂY --\n" +
                    "JOIN\n" +
                    "    matches m ON\n" +
                    "    -- Trường hợp 1: Người gửi thông báo là người gửi lời mời\n" +
                    "    (m.sender_id = n.sender_id AND m.receiver_id = un.user_id)\n" +
                    "    OR\n" +
                    "    -- Trường hợp 2: Người gửi thông báo là người nhận lời mời (trường hợp phản hồi)\n" +
                    "    (m.receiver_id = n.sender_id AND m.sender_id = un.user_id)\n" +
                    "-- SỬA ĐỔI KẾT THÚC --\n" +
                    "LEFT JOIN (\n" +
                    "    -- Subquery để lấy ảnh mới nhất của người gửi\n" +
                    "    SELECT user_id, photo_url FROM (\n" +
                    "        SELECT user_id, photo_url, ROW_NUMBER() OVER(PARTITION BY user_id ORDER BY upload_date DESC) as rn\n" +
                    "        FROM photos\n" +
                    "    ) AS ranked_photos\n" +
                    "    WHERE rn = 1\n" +
                    ") AS latest_photo ON n.sender_id = latest_photo.user_id\n" +
                    "WHERE\n" +
                    "    un.user_id = :userId\n" +
                    "    AND n.notification_type = 'MATCH'\n" +
                    "    AND un.is_read = FALSE;\n";


    public static final String GET_MESSAGE_NOTIFICATION_BY_USER_ID =
            "SELECT \n" +
                    "    n.notification_id as notificationId,\n" +
                    "    n.notification_type as notificationType,\n" +
                    "    n.content as content,\n" +
                    "    n.created_at as createAt,\n" +
                    "    u.user_id AS senderId,\n" +
                    "    p.photo_url AS profilePicture\n" +
                    "FROM user_notifications un\n" +
                    "JOIN notifications n ON un.notification_id = n.notification_id\n" +
                    "JOIN matches m ON m.sender_id = un.user_id AND m.receiver_id = :userId\n" +
                    "JOIN users u ON u.user_id = un.user_id\n" +
                    "LEFT JOIN photos p ON p.user_id = u.user_id\n" +
                    "                 AND p.is_profile_picture = TRUE\n" +
                    "                 AND p.upload_date = (\n" +
                    "                     SELECT MAX(upload_date)\n" +
                    "                     FROM photos\n" +
                    "                     WHERE user_id = u.user_id AND is_profile_picture = TRUE\n" +
                    "                 )\n" +
                    "WHERE un.user_id != :userId\n" +
                    "  AND un.user_id = m.sender_id\n" +
                    "  AND un.is_read = 'false' \n"+
                    "  AND n.notification_type = 'MESSAGE'\n";


    public static final String GET_LIKE_NOTIFICATION_BY_USER_ID =
            "SELECT \n" +
                    "    n.notification_id as notificationId,\n" +
                    "    n.notification_type as notificationType,\n" +
                    "    n.content as content,\n" +
                    "    n.created_at as createAt,\n" +
                    "    u.user_id AS senderId,\n" +
                    "    p.photo_url AS profilePicture\n" +
                    "FROM user_notifications un\n" +
                    "JOIN notifications n ON un.notification_id = n.notification_id\n" +
                    "JOIN matches m ON m.sender_id = un.user_id AND m.receiver_id = :userId\n" +
                    "JOIN users u ON u.user_id = un.user_id\n" +
                    "LEFT JOIN photos p ON p.user_id = u.user_id\n" +
                    "                 AND p.is_profile_picture = TRUE\n" +
                    "                 AND p.upload_date = (\n" +
                    "                     SELECT MAX(upload_date)\n" +
                    "                     FROM photos\n" +
                    "                     WHERE user_id = u.user_id AND is_profile_picture = TRUE\n" +
                    "                 )\n" +
                    "WHERE un.user_id != :userId\n" +
                    "  AND un.user_id = m.sender_id\n" +
                    "  AND un.is_read = 'false' \n"+
                    "  AND n.notification_type = 'LIKE'\n";

    public static final String GET_SYSTEM_NOTIFICATION_BY_USER_ID =
            "SELECT \n" +
                    "    n.notification_id as notificationId,\n" +
                    "    n.notification_type as notificationType,\n" +
                    "    n.content as content,\n" +
                    "    n.created_at as createAt,\n" +
                    "    u.user_id AS senderId,\n" +
                    "    p.photo_url AS profilePicture\n" +
                    "FROM user_notifications un\n" +
                    "JOIN notifications n ON un.notification_id = n.notification_id\n" +
                    "JOIN matches m ON m.sender_id = un.user_id AND m.receiver_id = :userId\n" +
                    "JOIN users u ON u.user_id = un.user_id\n" +
                    "LEFT JOIN photos p ON p.user_id = u.user_id\n" +
                    "                 AND p.is_profile_picture = TRUE\n" +
                    "                 AND p.upload_date = (\n" +
                    "                     SELECT MAX(upload_date)\n" +
                    "                     FROM photos\n" +
                    "                     WHERE user_id = u.user_id AND is_profile_picture = TRUE\n" +
                    "                 )\n" +
                    "WHERE un.user_id != :userId\n" +
                    "  AND un.user_id = m.sender_id\n" +
                    "  AND un.is_read = 'false' \n"+
                    "  AND n.notification_type = 'SYSTEM'\n" ;

    public static final String GET_POST_NOTIFICATION_BY_USER_ID =
            "SELECT \n" +
                    "    n.notification_id as notificationId,\n" +
                    "    n.notification_type as notificationType,\n" +
                    "    n.content as content,\n" +
                    "    n.created_at as createAt,\n" +
                    "    u.user_id AS senderId,\n" +
                    "    p.photo_url AS profilePicture\n" +
                    "FROM user_notifications un\n" +
                    "JOIN notifications n ON un.notification_id = n.notification_id\n" +
                    "JOIN matches m ON m.sender_id = un.user_id AND m.receiver_id = :userId\n" +
                    "JOIN users u ON u.user_id = un.user_id\n" +
                    "LEFT JOIN photos p ON p.user_id = u.user_id\n" +
                    "                 AND p.is_profile_picture = TRUE\n" +
                    "                 AND p.upload_date = (\n" +
                    "                     SELECT MAX(upload_date)\n" +
                    "                     FROM photos\n" +
                    "                     WHERE user_id = u.user_id AND is_profile_picture = TRUE\n" +
                    "                 )\n" +
                    "WHERE un.user_id != :userId\n" +
                    "  AND un.user_id = m.sender_id\n" +
                    "  AND n.notification_type = 'POST'\n" +
                    "  AND un.is_read = 'false' \n"+
                    "  AND m.status = 'MATCHED';";

}
