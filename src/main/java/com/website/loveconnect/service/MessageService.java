package com.website.loveconnect.service;

import com.website.loveconnect.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessageBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
}
