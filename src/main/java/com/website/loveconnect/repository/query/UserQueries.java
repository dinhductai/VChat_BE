package com.website.loveconnect.repository.query;

import org.apache.commons.beanutils.PropertyUtilsBean;

public  class UserQueries {
    public static final String GET_USER_BY_ID = "SELECT \n" +
            "u.user_id AS userId, \n" +
            "p.photo_url AS photoUrl, \n" +
            "up.full_name AS fullName, \n" +
            "u.email AS email, \n" +
            "up.gender AS gender, \n" +
            "up.location AS location, \n" +
            "up.description AS description, \n" +
            "GROUP_CONCAT(i.interest_name ORDER BY i.interest_name SEPARATOR ', ') AS interests, \n" +
            "u.registration_date AS registrationDate, \n" +
            "up.birthdate AS birthDate, \n" +
            "u.phone_number AS phoneNumber, \n" +
            "u.account_status AS accountStatus, \n" +
            "p.upload_date AS uploadDate \n" +
            "FROM users u \n" +
            "LEFT JOIN user_profiles up ON up.user_id = u.user_id \n" +
            "LEFT JOIN photos p ON p.user_id = u.user_id AND p.is_profile_picture = 1 AND p.is_approved = 1 \n" +
            "LEFT JOIN user_interests ui ON u.user_id = ui.user_id \n" +
            "LEFT JOIN interests i ON ui.interest_id = i.interest_id \n" +
            "WHERE u.user_id = :idUser \n" +
            "GROUP BY u.user_id, p.photo_url, up.full_name, u.email, up.gender, up.location, up.description, \n" +
            "u.registration_date, up.birthdate, u.phone_number, u.account_status, p.upload_date \n" +
            "ORDER BY p.upload_date DESC \n" +
            "LIMIT 1;";

    public static final String GET_USER_FOR_UPDATE_BY_ID = "SELECT \n" +
            "u.user_id AS userId, \n" +
            "up.full_name AS fullName, \n" +
            "up.birthdate AS birthDate, \n" +
            "up.location AS location, \n" +
            "up.description AS description, \n" +
            "GROUP_CONCAT(i.interest_name ORDER BY i.interest_name SEPARATOR ', ') AS interests, \n" +
            "p.photo_url AS photoUrl, \n" +
            "u.phone_number AS phoneNumber, \n" +
            "u.email AS email, \n" +
            "up.gender AS gender, \n" +
            "u.account_status AS accountStatus, \n" +
            "p.upload_date AS uploadDate \n" +
            "FROM users u \n" +
            "LEFT JOIN user_profiles up ON up.user_id = u.user_id \n" +
            "LEFT JOIN photos p ON p.user_id = u.user_id AND p.is_profile_picture = 1 AND p.is_approved = 1 \n" +
            "LEFT JOIN user_interests ui ON u.user_id = ui.user_id \n" +
            "LEFT JOIN interests i ON ui.interest_id = i.interest_id \n" +
            "WHERE u.user_id = :idUser \n" +
            "GROUP BY u.user_id, up.full_name, up.birthdate, up.location, up.description, \n" +
            "p.photo_url, u.phone_number, u.email, up.gender, u.account_status, p.upload_date \n" +
            "ORDER BY p.upload_date DESC \n" +
            "LIMIT 1 ; ";

    public static final String GET_USER_BY_FILTERS =
            "Select u.user_id as userId, up.full_name as fullName, u.email as email, " +
                    "u.phone_number as phone, u.registration_date as registrationDate, u.account_status as accountStatus " +
                    "from users u " +
                    "join user_profiles up on up.user_id = u.user_id " +
                    "where u.account_status <> 'DELETED' " +
                    "and (:status IS NULL OR u.account_status = :status ) " +
                    "and (:gender IS NULL OR up.gender = :gender ) " +
                    "and (:keyword IS NULL OR up.full_name LIKE CONCAT('%', :keyword ,'%') ) " +
                    "ORDER BY " +
                    "CASE WHEN :sort = 'newest' THEN u.registration_date END DESC, " +
                    "CASE WHEN :sort = 'oldest' THEN u.registration_date END ASC, " +
                    "CASE WHEN :sort = 'name_asc' THEN up.full_name END ASC, " + //A-Z
                    "CASE WHEN :sort = 'name_desc' THEN up.full_name END DESC"; //từ Z-A

    public static final String EXIST_USER_BY_ROLE_ADMIN_AND_STATUS_ACTIVE =
            "SELECT EXISTS ( SELECT 1 FROM users u " +
                    "JOIN user_roles ur ON u.user_id = ur.user_id " +
                    "JOIN roles r ON ur.role_id = r.role_id " +
                    "WHERE r.role_name = 'ADMIN' " +
                    "AND u.account_status = 'ACTIVE' " +
                    ") AS admin_exists ";

    public static final String GET_ALL_USER =
            " select u.user_id, up.full_name, u.email, " +
                    " u.phone_number, u.registration_date, u.account_status " +
                    "from users u " +
                    "join user_profiles up on up.user_id = u.user_id " +
                    "where u.account_status <> 'DELETED' ";

    public static final String GET_ALL_USER_ROLE_BY_USERID =
            "SELECT  r.role_name " +
                    "FROM users u " +
                    "INNER JOIN user_roles ur ON u.user_id = ur.user_id " +
                    "INNER JOIN roles r ON ur.role_id = r.role_id " +
                    "WHERE u.user_id = :idUser ;";

    public static final String GET_ALL_USER_BY_KEYWORD =
            "SELECT DISTINCT\n" +
                    "    u.user_id AS userId,\n" +
                    "    u.email AS email,\n" +
                    "    u.phone_number AS phoneNumber,\n" +
                    "    up.full_name AS fullName,\n" +
                    "    up.birthdate AS birthDate,\n" +
                    "    up.gender AS gender,\n" +
                    "    up.looking_for AS lookingFor,\n" +
                    "    up.bio AS bio,\n" +
                    "    up.height AS height,\n" +
                    "    up.weight AS weight,\n" +
                    "    up.location AS location,\n" +
                    "    up.job_title AS jobTitle,\n" +
                    "    up.company AS company,\n" +
                    "    up.education AS education,\n" +
                    "    up.bio AS description,\n" +
                    "    GROUP_CONCAT(DISTINCT i.interest_name) AS interestName\n" +
                    "FROM \n" +
                    "    users u\n" +
                    "    INNER JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "    LEFT JOIN user_interests ui ON u.user_id = ui.user_id\n" +
                    "    LEFT JOIN interests i ON ui.interest_id = i.interest_id\n" +
                    "WHERE \n" +
                    "    u.email LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR u.phone_number LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR up.full_name LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR up.bio LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR up.location LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR up.job_title LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR up.company LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR up.education LIKE CONCAT('%', :keyword, '%')\n" +
                    "    OR i.interest_name LIKE CONCAT('%', :keyword, '%')\n" +
                    "GROUP BY \n" +
                    "    u.user_id,\n" +
                    "    u.email,\n" +
                    "    u.phone_number,\n" +
                    "    up.full_name,\n" +
                    "    up.birthdate,\n" +
                    "    up.gender,\n" +
                    "    up.looking_for,\n" +
                    "    up.bio,\n" +
                    "    up.height,\n" +
                    "    up.weight,\n" +
                    "    up.location,\n" +
                    "    up.job_title,\n" +
                    "    up.company,\n" +
                    "    up.education\n" +
                    "ORDER BY \n" +
                    "    u.user_id ;";

    public static final String GET_USER_AND_PHOTO_HOME_PAGE =
            "SELECT \n" +
                    "u.user_id AS userId,\n" +
                    "up.full_name AS fullName,\n" +
                    "up.location AS location,\n" +
                    "up.gender AS gender,\n" +
                    "GROUP_CONCAT(p.photo_url) AS photos\n" +
                    "FROM users u\n" +
                    "LEFT JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN photos p ON u.user_id = p.user_id\n" +
                    "WHERE \n" +
                    "    u.account_status = 'ACTIVE'\n" +
                    "    AND up.gender = :lookingFor \n" +
                    "GROUP BY u.user_id, up.full_name, up.location, up.gender ";

    public static final String GET_USER_FRIENDS =
            "SELECT DISTINCT \n" +
                    "    u.user_id AS user_id,\n" +
                    "    up.full_name AS user_fullname,\n" +
                    "    p.photo_url AS photoUrl,\n" +
                    "    up.bio,\n" +
                    "    u.phone_number\n" +
                    "FROM matches m\n" +
                    "JOIN users u \n" +
                    "    ON u.user_id = (\n" +
                    "        CASE \n" +
                    "            WHEN m.sender_id = :userId THEN m.receiver_id\n" +
                    "            ELSE m.sender_id\n" +
                    "        END\n" +
                    "    )\n" +
                    "LEFT JOIN user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT p1.*\n" +
                    "    FROM photos p1\n" +
                    "    JOIN (\n" +
                    "        SELECT user_id, MAX(upload_date) AS max_upload_date\n" +
                    "        FROM photos\n" +
                    "        WHERE is_profile_picture = true\n" +
                    "        GROUP BY user_id\n" +
                    "    ) latest ON p1.user_id = latest.user_id AND p1.upload_date = latest.max_upload_date\n" +
                    "    WHERE p1.is_profile_picture = true\n" +
                    ") p ON p.user_id = u.user_id\n" +
                    "WHERE \n" +
                    "    (m.sender_id = :userId OR m.receiver_id = :userId)\n" +
                    "    AND m.status = 'MATCHED' ";

    public static final String GET_FRIENDS_MATCHED =
            "SELECT\n" +
                    "    friends.friend_id AS userId,\n" +
                    "    up.full_name AS fullName,\n" +
                    "    up.bio AS bio,\n" +
                    "    u.phone_number AS phoneNumber,\n" +
                    "    (\n" +
                    "        SELECT p.photo_url\n" +
                    "        FROM photos p\n" +
                    "        WHERE p.user_id = friends.friend_id\n" +
                    "        ORDER BY p.upload_date DESC\n" +
                    "        LIMIT 1\n" +
                    "    ) AS photoProfile\n" +
                    "FROM\n" +
                    "    (\n" +
                    "        SELECT receiver_id AS friend_id, match_date\n" +
                    "        FROM matches\n" +
                    "        WHERE sender_id = :userId AND status = 'MATCHED'\n" +
                    "        UNION\n" +
                    "        SELECT sender_id AS friend_id, match_date\n" +
                    "        FROM matches\n" +
                    "        WHERE receiver_id = :userId AND status = 'MATCHED'\n" +
                    "    ) AS friends\n" +
                    "JOIN\n" +
                    "    users AS u ON friends.friend_id = u.user_id\n" +
                    "JOIN\n" +
                    "    user_profiles AS up ON friends.friend_id = up.user_id\n" +
                    "ORDER BY\n" +
                    "    friends.match_date DESC ";

    public static final String GET_FRIENDS_FRIENDS =
            "WITH MyFriends AS (\n" +
                    "    SELECT receiver_id AS friend_id\n" +
                    "    FROM matches\n" +
                    "    WHERE sender_id = :userId AND status = 'MATCHED'\n" +
                    "    UNION\n" +
                    "    SELECT sender_id AS friend_id\n" +
                    "    FROM matches\n" +
                    "    WHERE receiver_id = :userId AND status = 'MATCHED'\n" +
                    "),\n" +
                    "FriendsOfFriends AS (\n" +
                    "    SELECT m.receiver_id AS fof_id\n" +
                    "    FROM matches m\n" +
                    "    JOIN MyFriends mf ON m.sender_id = mf.friend_id\n" +
                    "    WHERE m.status = 'MATCHED'\n" +
                    "    UNION\n" +
                    "    SELECT m.sender_id AS fof_id\n" +
                    "    FROM matches m\n" +
                    "    JOIN MyFriends mf ON m.receiver_id = mf.friend_id\n" +
                    "    WHERE m.status = 'MATCHED'\n" +
                    ")\n" +
                    "SELECT DISTINCT\n" +
                    "    fof.fof_id AS userId,\n" +
                    "    up.full_name AS fullName,\n" +
                    "    up.bio AS bio,\n" +
                    "    u.phone_number AS phoneNumber,\n" +
                    "    (\n" +
                    "        SELECT p.photo_url\n" +
                    "        FROM photos p\n" +
                    "        WHERE p.user_id = fof.fof_id\n" +
                    "        ORDER BY p.upload_date DESC\n" +
                    "        LIMIT 1\n" +
                    "    ) AS photoProfile\n" +
                    "FROM\n" +
                    "    FriendsOfFriends fof\n" +
                    "JOIN\n" +
                    "    users AS u ON fof.fof_id = u.user_id\n" +
                    "JOIN\n" +
                    "    user_profiles AS up ON fof.fof_id = up.user_id\n" +
                    "WHERE\n" +
                    "    fof.fof_id != :userId\n" +
                    "    AND fof.fof_id NOT IN (SELECT friend_id FROM MyFriends);";


    public static final String GET_RANDOM_FRIENDS =
            "SELECT \n" +
                    "    u.user_id As userId,\n" +
                    "    up.full_name AS fullName,\n" +
                    "    up.bio as bio,\n" +
                    "    u.phone_number as phoneNumber,\n" +
                    "    p.photo_url AS photoProfile\n" +
                    "FROM \n" +
                    "    users u\n" +
                    "JOIN \n" +
                    "    user_profiles up ON u.user_id = up.user_id\n" +
                    "LEFT JOIN \n" +
                    "    (\n" +
                    "        SELECT \n" +
                    "            p1.user_id, \n" +
                    "            p1.photo_url\n" +
                    "        FROM \n" +
                    "            photos p1\n" +
                    "        JOIN \n" +
                    "            (\n" +
                    "                SELECT \n" +
                    "                    user_id, \n" +
                    "                    MAX(upload_date) AS max_upload_date\n" +
                    "                FROM \n" +
                    "                    photos\n" +
                    "                WHERE \n" +
                    "                    is_profile_picture = TRUE\n" +
                    "                GROUP BY \n" +
                    "                    user_id\n" +
                    "            ) latest ON p1.user_id = latest.user_id AND p1.upload_date = latest.max_upload_date\n" +
                    "        WHERE \n" +
                    "            p1.is_profile_picture = TRUE\n" +
                    "    ) p ON p.user_id = u.user_id\n" +
                    "WHERE \n" +
                    "    u.user_id != :userId \n" +
                    "    AND u.account_status = 'ACTIVE'\n" +
                    "    AND u.user_id NOT IN (\n" +
                    "        SELECT receiver_id FROM matches WHERE sender_id = :userId AND status IN ('MATCHED', 'PENDING')\n" +
                    "        UNION\n" +
                    "        SELECT sender_id FROM matches WHERE receiver_id = :userId AND status IN ('MATCHED', 'PENDING')\n" +
                    "    )\n" +
                    "ORDER BY " +
                    "   RAND() ;";

    public static final String GET_FRIENDS_PENDING =
            "SELECT\n" +
                    "    friends.friend_id AS userId,\n" +
                    "    up.full_name AS fullName,\n" +
                    "    up.bio AS bio,\n" +
                    "    u.phone_number AS phoneNumber,\n" +
                    "    (\n" +
                    "        SELECT p.photo_url\n" +
                    "        FROM photos p\n" +
                    "        WHERE p.user_id = friends.friend_id\n" +
                    "        ORDER BY p.upload_date DESC\n" +
                    "        LIMIT 1\n" +
                    "    ) AS photoProfile\n" +
                    "FROM\n" +
                    "    (\n" +
                    "        SELECT receiver_id AS friend_id, match_date\n" +
                    "        FROM matches\n" +
                    "        WHERE sender_id = :userId AND status = 'PENDING'\n" +
                    "        UNION\n" +
                    "        SELECT sender_id AS friend_id, match_date\n" +
                    "        FROM matches\n" +
                    "        WHERE receiver_id = :userId AND status = 'PENDING'\n" +
                    "    ) AS friends\n" +
                    "JOIN\n" +
                    "    users AS u ON friends.friend_id = u.user_id\n" +
                    "JOIN\n" +
                    "    user_profiles AS up ON friends.friend_id = up.user_id\n" +
                    "ORDER BY\n" +
                    "    friends.match_date DESC ";

}
