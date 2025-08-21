package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.MessageResponse;
import com.website.loveconnect.entity.Message;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class MessageMapper {
    public MessageResponse toMessageResponse(Tuple tuple){
        return MessageResponse.builder()
                .senderId(tuple.get("senderId",Integer.class))
                .receiverId(tuple.get("receiverId",Integer.class))
                .messageId(tuple.get("messageId",Integer.class))
                .message(tuple.get("message",String.class))
                .sentAt(tuple.get("sentAt", Timestamp.class))
                .isDeleted(tuple.get("isDeleted",Boolean.class))
                .build();
    }
}
