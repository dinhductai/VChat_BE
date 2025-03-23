package com.website.loveconnect.repository.custom.query;

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
}
