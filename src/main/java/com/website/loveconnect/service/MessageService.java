package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.MessageDeleteRequest;
import com.website.loveconnect.dto.request.MessageLoadRequest;
import com.website.loveconnect.dto.request.MessageRequest;
import com.website.loveconnect.dto.request.MessageUpdateRequest;
import com.website.loveconnect.dto.response.MessageResponse;
import com.website.loveconnect.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessageBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
    Page<MessageResponse> getAllMessageBySenderIdAndReceiverId(Integer senderId,Integer receiverId, int page,int size);
    MessageResponse createMessage(MessageRequest messageRequest,Integer senderId);
    MessageResponse updateMessage(MessageUpdateRequest messageRequest,Integer senderId);
    MessageResponse deleteMessage(MessageDeleteRequest messageDeleteRequest, Integer senderId);
}
