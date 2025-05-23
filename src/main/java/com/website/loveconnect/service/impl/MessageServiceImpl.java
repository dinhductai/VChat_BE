package com.website.loveconnect.service.impl;

import com.website.loveconnect.entity.Message;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.MessageRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class MessageServiceImpl implements MessageService {
    MessageRepository messageRepository;
    UserRepository UserRepository;
    private final UserRepository userRepository;

    @Override
    public List<Message> getAllMessageBySenderIdAndReceiverId(Integer senderId, Integer receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(()-> new UserNotFoundException("Sender cannot found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(()-> new UserNotFoundException("Receiver cannot found"));
        List<Message> listMessage = messageRepository.findAllBySenderAndReceiver(sender,receiver);
        return listMessage;
    }
}
