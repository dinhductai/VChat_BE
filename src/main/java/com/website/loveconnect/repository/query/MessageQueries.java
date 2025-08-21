package com.website.loveconnect.repository.query;

public class MessageQueries {
    public static final String GET_MESSAGES_BY_SENDER_AND_RECEIVER_ID =
            "SELECT\n" +
                    "    m.sender_id as senderId,\n" +
                    "    m.receiver_id as receiverId,\n" +
                    "    m.message_id as messageId,\n"+
                    "    m.message_text as message,\n" +
                    "    m.sent_at as sentAt,\n" +
                    "    m.is_delete as isDeleted\n" +
                    "FROM\n" +
                    "    messages m\n" +
                    "WHERE\n" +
                    "    m.match_id = (\n" +
                    "        SELECT match_id\n" +
                    "        FROM matches\n" +
                    "        WHERE\n" +
                    "            ((sender_id = :senderId AND receiver_id = :receiverId) OR (sender_id = :receiverId AND receiver_id = :senderId))\n" +
                    "            AND status = 'MATCHED'\n" +
                    "    )\n" +
                    "ORDER BY\n" +
                    "    m.sent_at ASC;";
}
