package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Notification;
import com.website.loveconnect.repository.query.NotificationQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query(value = NotificationQueries.GET_MATCH_NOTIFICATION_BY_USER_ID,nativeQuery = true)
    List<Tuple> getMatchNotificationByUserId(@Param("userId") Integer userId);

    @Query(value = NotificationQueries.GET_SYSTEM_NOTIFICATION_BY_USER_ID,nativeQuery = true)
    List<Tuple> getSystemNotificationByUserId(@Param("userId") Integer userId);

    @Query(value = NotificationQueries.GET_MESSAGE_NOTIFICATION_BY_USER_ID,nativeQuery = true)
    List<Tuple> getMessageNotificationByUserId(@Param("userId") Integer userId);

    @Query(value = NotificationQueries.GET_LIKE_NOTIFICATION_BY_USER_ID,nativeQuery = true)
    List<Tuple> getLikeNotificationByUserId(@Param("userId") Integer userId);

    @Query(value = NotificationQueries.GET_POST_NOTIFICATION_BY_USER_ID,nativeQuery = true)
    List<Tuple> getPostNotificationByUserId(@Param("userId") Integer userId);

    void deleteNotificationByNotificationIdIn(Long[] notificationIds);

}
