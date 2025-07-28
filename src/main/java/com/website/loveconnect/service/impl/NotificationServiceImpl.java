package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.NotificationResponse;
import com.website.loveconnect.entity.*;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.enumpackage.NotificationType;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.NotificationNotFoundException;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.config.http.MatcherType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
    public void createNotificationRequestFriend(User sender, User receiver, MatchStatus status) {
        try {
            UserProfile userProfile = userProfileRepository.findByUser_UserId(sender.getUserId())
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            Notification notification = new Notification();
                notification.setSender(sender);
                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                notification.setNotificationType(NotificationType.MATCH);
                if(status.equals(MatchStatus.PENDING)){
                    notification.setContent(userProfile.getFullName() + " just sent you a friend request");
                }else if(status.equals(MatchStatus.MATCHED)){
                        notification.setContent(userProfile.getFullName() + " accept your friend request");
                    }
                    else if(status.equals(MatchStatus.REJECTED)){
                        notification.setContent(userProfile.getFullName() + " reject your friend request");
                }
            notificationRepository.save(notification);
            UserNotification userNotification = UserNotification.builder()
                    .user(receiver)
                    .notification(notification)
                    .isRead(false)
                    .build();
            userNotificationRepository.save(userNotification);
        }catch (DataAccessException da){
            throw  new DataAccessException("Cannot access database");
        }
    }

    @Override
    public Page<NotificationResponse> getNewNotifications(Integer userId,int page,int size) {
        try{
            User user =  userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
            List<NotificationResponse> getMatchNotification = notificationRepository.getMatchNotificationByUserId(user.getUserId())
                    .stream().map(notificationMapper::toNotificationResponse).toList();
            List<NotificationResponse> getPostNotification = notificationRepository.getPostNotificationByUserId(user.getUserId())
                    .stream().map(notificationMapper::toNotificationResponse).toList();
            List<NotificationResponse> getLikeNotification = notificationRepository.getLikeNotificationByUserId(user.getUserId())
                    .stream().map(notificationMapper::toNotificationResponse).toList();
            List<NotificationResponse> getSystemNotification = notificationRepository.getSystemNotificationByUserId(user.getUserId())
                    .stream().map(notificationMapper::toNotificationResponse).toList();

            List<NotificationResponse> newNotification = new ArrayList<>();
            newNotification.addAll(getMatchNotification);
            newNotification.addAll(getPostNotification);
            newNotification.addAll(getLikeNotification);
            newNotification.addAll(getSystemNotification);
            newNotification.sort((a,b) -> b.getCreateAt().compareTo(a.getCreateAt()));

            int start = page*size;
            int end = Math.min(start+size,getMatchNotification.size());

            //nếu trang vượt quá dữ liệu, trả về rỗng
            if (start >= newNotification.size()) {
                return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), newNotification.size());
            }

            //cắt danh sách để trả về trang hiện tại
            List<NotificationResponse> pageContent = newNotification.subList(start, end);

            //trả về Page
            return new PageImpl<>(pageContent, PageRequest.of(page, size), newNotification.size());

        }catch (DataAccessException de){
            throw  new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public void readNotification(Integer userId,Integer[] notificationIds) {
        try{
            User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
            for(Integer id : notificationIds){
                Notification notification = notificationRepository.findById(id).orElseThrow(()->new NotificationNotFoundException("Notification not found"));
                UserNotification userNotification = userNotificationRepository
                        .findUserNotificationByUserAndNotification(user,notification)
                        .orElseThrow(()->new NotificationNotFoundException("Notification not found"));
                userNotification.setIsRead(true);
                userNotificationRepository.save(userNotification);
            }
        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }
    }


}
