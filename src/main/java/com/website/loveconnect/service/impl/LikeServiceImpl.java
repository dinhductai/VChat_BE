package com.website.loveconnect.service.impl;

import com.website.loveconnect.entity.Like;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.LikeStatus;
import com.website.loveconnect.exception.LikeDuplicatedException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.LikeRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.LikeService;
import com.website.loveconnect.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class LikeServiceImpl implements LikeService {
    LikeRepository likeRepository;
    UserRepository userRepository;
    MatchService matchService;

    @Override
    public void likeUserById(Integer senderId, Integer receiverId) {
        doLikeOrDislike(senderId,receiverId,LikeStatus.ACTIVE);
    }

    @Override
    public void dislikeUserById(Integer senderId, Integer receiverId) {
        doLikeOrDislike(senderId,receiverId,LikeStatus.INACTIVE);
    }

    public void doLikeOrDislike(Integer senderId, Integer receiverId,LikeStatus status) {
        if(senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Sender and Receiver id cannot be null");
        }
        try {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new UserNotFoundException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new UserNotFoundException("Receiver not found"));
            checkExistAndStatusToSave(sender,receiver,status);
        }catch (DataAccessException e) {
            log.error("Lỗi truy vấn cơ sở dữ liệu: {}", e.getMessage());
            throw new RuntimeException("Lỗi truy vấn cơ sở dữ liệu");
        }
    }

    public void checkExistAndStatusToSave(User sender, User receiver, LikeStatus status) {
        if(status == LikeStatus.ACTIVE) {
            boolean likeExisted = likeRepository.existsBySenderAndReceiver(sender,receiver);
            if(!likeExisted) {
                Like like = Like.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .likeDate(new Timestamp(System.currentTimeMillis()))
                        .likeStatus(status)
                        .build();
                likeRepository.save(like);
                matchService.createMatchByLike(sender,receiver);


            }else {
                throw new LikeDuplicatedException("Like already exists");
            }
        }else if(status == LikeStatus.INACTIVE) {
            boolean likeExisted = likeRepository.existsBySenderAndReceiver(sender,receiver);
            if(likeExisted) {
                Like like = likeRepository.findBySenderAndReceiver(sender,receiver);
                like.setLikeStatus(status);
                likeRepository.save(like);
            }else {
                throw new LikeDuplicatedException("UnLike already do");
            }
        }
    }

}
