package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.MessageDeleteRequest;
import com.website.loveconnect.dto.request.MessageLoadRequest;
import com.website.loveconnect.dto.request.MessageRequest;
import com.website.loveconnect.dto.request.MessageUpdateRequest;
import com.website.loveconnect.dto.response.MessageResponse;
import com.website.loveconnect.entity.Match;
import com.website.loveconnect.entity.Message;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.exception.*;
import com.website.loveconnect.mapper.MessageMapper;
import com.website.loveconnect.repository.MatchRepository;
import com.website.loveconnect.repository.MessageRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class MessageServiceImpl implements MessageService {
    MessageRepository messageRepository;
    UserRepository UserRepository;
    UserRepository userRepository;
    MatchRepository matchRepository;
    MessageMapper messageMapper;

    @Override
    public List<Message> getAllMessageBySenderIdAndReceiverId(Integer senderId, Integer receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(()-> new UserNotFoundException("Sender cannot found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(()-> new UserNotFoundException("Receiver cannot found"));
        List<Message> listMessage = messageRepository.findAllBySenderAndReceiver(sender,receiver);
        return listMessage;
    }

    @Override
    public Page<MessageResponse> getAllMessageBySenderIdAndReceiverId(Integer senderId,Integer receiverId, int page,int size) {
        try{
            Pageable pageable = PageRequest.of(page,size);
            Page<MessageResponse> messageResponses = messageRepository.findAllMessageBySenderIdAndReceiverId(senderId,
                            receiverId, pageable)
                    .map(messageMapper::toMessageResponse);
            return messageResponses;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MessageResponse createMessage(MessageRequest messageRequest,Integer senderId) {
        try{
            User sender = userRepository.findById(senderId)
                    .orElseThrow(()-> new UserNotFoundException("Sender cannot found"));
            User receiver = userRepository.findById(messageRequest.getReceiverId())
                    .orElseThrow(()-> new UserNotFoundException("Receiver cannot found"));
            Optional<Match> matchOptional = matchRepository.findBySenderAndReceiver(sender, receiver);
            if (matchOptional.isEmpty()) {
                matchOptional = matchRepository.findBySenderAndReceiver(receiver, sender);
            }
            Match match = matchOptional.orElseThrow(() -> new MatchNotFoundException("Match cannot found"));

            Message message = Message.builder()
                    .match(match)
                    .sender(sender)
                    .receiver(receiver)
                    .messageText(messageRequest.getMessage())
                    .sentAt(new Timestamp(System.currentTimeMillis()))
                    .isDelete(Boolean.FALSE)
                    .build();
            messageRepository.save(message);
            return MessageResponse.builder()
                    .senderId(sender.getUserId())
                    .receiverId(receiver.getUserId())
                    .messageId(message.getMessageId())
                    .message(message.getMessageText())
                    .sentAt(message.getSentAt())
                    .isDeleted(message.getIsDelete())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MessageResponse updateMessage(MessageUpdateRequest messageRequest,Integer senderId) {
        try{
            User sender = userRepository.findById(senderId)
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            User receiver =  userRepository.findById(messageRequest.getReceiverId())
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            Message message = messageRepository.findById(messageRequest.getMessageId())
                    .orElseThrow(()-> new MessageNotFoundException("Message not found"));

            if(!message.getSender().getUserId().equals(sender.getUserId())){
                throw new ForbiddenException("You are not authorized to edit this message");
            }else if(message.getIsDelete()){
                throw new MessageAlreadyDeleted("Message already deleted");
            }
            message.setMessageText(messageRequest.getMessage());
            messageRepository.save(message);
            return MessageResponse.builder()
                    .senderId(sender.getUserId())
                    .receiverId(receiver.getUserId())
                    .messageId(message.getMessageId())
                    .message(message.getMessageText())
                    .isDeleted(message.getIsDelete())
                    .build();
        }
        catch (DataAccessException e){
            throw  new DataAccessException("Cannot access database");
        }
    }

    @Override
    public MessageResponse deleteMessage(MessageDeleteRequest messageRequest, Integer senderId) {
        try{
            User sender = userRepository.findById(senderId)
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            User receiver =  userRepository.findById(messageRequest.getReceiverId())
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            Message message = messageRepository.findById(messageRequest.getMessageId())
                    .orElseThrow(()-> new MessageNotFoundException("Message not found"));

            if(!message.getSender().getUserId().equals(sender.getUserId())){
                throw new ForbiddenException("You are not authorized to edit this message");
            }else if(message.getIsDelete()){
                throw new MessageAlreadyDeleted("Message already deleted");
            }
            message.setMessageText("Message deleted");
            message.setIsDelete(Boolean.TRUE);
            messageRepository.save(message);
            return MessageResponse.builder()
                    .senderId(sender.getUserId())
                    .receiverId(receiver.getUserId())
                    .messageId(message.getMessageId())
                    .message(message.getMessageText())
                    .isDeleted(message.getIsDelete())
                    .build();

        }catch (DataAccessException e){
            throw  new DataAccessException("Cannot access database");

        }
    }
}
