package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Notification;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserNotification;
import com.website.loveconnect.enumpackage.NotificationType;
import com.website.loveconnect.repository.query.NotificationQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    @Query(value = NotificationQueries.GET_MATCH_NOTIFICATION_BY_USER_ID,nativeQuery = true)
    List<Tuple> getMatchNotificationByUserId(@Param("userId") String userId);

    Optional<UserNotification> findUserNotificationByUserAndNotification(User user, Notification notification);
}
