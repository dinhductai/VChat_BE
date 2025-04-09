package com.website.loveconnect.repository.query;

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
}
