package com.website.loveconnect.repository.query;

import org.springframework.stereotype.Component;


public class MatchQueries {
    public static final String GET_ALL_MATCH_BY_SENDER_ID =
            "SELECT m.match_date as matchDate, m.status as status ,up.full_name as fullName, " +
                    "up.gender as gender, up.description as description " +
                    "FROM matches m " +
                    "JOIN users u ON m.receiver_id = u.user_id " +
                    "JOIN user_profiles up ON u.user_id = up.user_id " +
                    "WHERE m.sender_id = :senderId ;";

    public static final String GET_MATCH_BY_MATCH_ID =
            "SELECT m.match_date as matchDate, m.status as status ,up.full_name as fullName, " +
                    "up.gender as gender, up.description as description " +
                    "FROM matches m " +
                    "JOIN users u ON m.receiver_id = u.user_id " +
                    "JOIN user_profiles up ON u.user_id = up.user_id " +
                    "WHERE m.match_id = :matchId ;";

}
