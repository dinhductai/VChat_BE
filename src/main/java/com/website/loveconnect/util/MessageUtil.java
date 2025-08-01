package com.website.loveconnect.util;

import org.springframework.stereotype.Component;


public class MessageUtil {
    public static String createChatChannel(int user1, int user2) {
        if (user1 > user2) {
            int temp = user1;
            user1 = user2;
            user2 = temp;
        }
        return String.format("/topic/chat/%d-%d", user1, user2);
    }
}
