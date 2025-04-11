package com.website.loveconnect.repository.query;

public class PhotoQueries {
    public static final String FIND_OWNED_PHOTO =
            "SELECT p.photo_url " +
                    "From photos p " +
                    "Join users u on u.user_id = p.user_id " +
                    "where u.user_id = :idUser " +
                    "order by p.is_profile_picture desc, p.upload_date asc ";
}
