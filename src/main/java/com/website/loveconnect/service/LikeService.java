package com.website.loveconnect.service;

public interface LikeService {
    void likeUserById(Integer senderId, Integer receiverId);
    void dislikeUserById(Integer senderId, Integer receiverId);
}
