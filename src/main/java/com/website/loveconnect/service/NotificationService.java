package com.website.loveconnect.service;

import com.website.loveconnect.dto.response.NotificationResponse;
import com.website.loveconnect.entity.Match;
import com.website.loveconnect.entity.Notification;
import com.website.loveconnect.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    void createNotificationRequestFriend(User user);
    List<NotificationResponse> getNewNotifications(Integer userId);
}
