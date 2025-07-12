package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.NotificationResponse;
import com.website.loveconnect.entity.*;
import com.website.loveconnect.enumpackage.NotificationType;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.NotificationMapper;
import com.website.loveconnect.repository.NotificationRepository;
import com.website.loveconnect.repository.UserNotificationRepository;
import com.website.loveconnect.repository.UserProfileRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class NotificationServiceImpl implements NotificationService {
    NotificationRepository notificationRepository;
    UserNotificationRepository userNotificationRepository;
    UserRepository userRepository;
    UserProfileRepository userProfileRepository;
    NotificationMapper notificationMapper;
    @Override
    public void createNotificationRequestFriend(User user) {
        try {
            UserProfile userProfile = userProfileRepository.findByUser_UserId(user.getUserId())
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.MATCH)
                    .content(userProfile.getFullName() + " just sent you a friend request")
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            notificationRepository.save(notification);
            UserNotification userNotification = UserNotification.builder()
                    .user(user)
                    .notification(notification)
                    .build();
            userNotificationRepository.save(userNotification);
        }catch (DataAccessException da){
            throw  new DataAccessException("Cannot access database");
        }
    }

    @Override
    public List<NotificationResponse> getNewNotifications(Integer userId) {
        try{
            User user =  userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
            List<NotificationResponse> getMatchNotification = notificationRepository.getMatchNotificationByUserId(user.getUserId())
                    .stream().map(notificationMapper::toNotificationResponse).toList();
            return  getMatchNotification;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }


}
