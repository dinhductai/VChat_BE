package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.MatchRequestDTO;
import com.website.loveconnect.dto.response.*;
import com.website.loveconnect.entity.Match;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.MatchResponseType;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.MatchAlreadyExistingException;
import com.website.loveconnect.exception.MatchNotFoundException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.MatchMapper;
import com.website.loveconnect.repository.LikeRepository;
import com.website.loveconnect.repository.MatchRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.MatchService;
import com.website.loveconnect.service.NotificationService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional //đảm bảo toàn vẹn dữ liệu ở vài hàm cần thiết
public class MatchServiceImpl implements MatchService {
    MatchRepository matchRepository;
    UserRepository userRepository;
    LikeRepository likeRepository;
    MatchMapper matchMapper;
    NotificationService notificationService;

    @Override
    public MatchResponse createMatch(MatchRequestDTO matchRequestDTO) {
        User sender = userRepository.findById(matchRequestDTO.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User receiver = userRepository.findById(matchRequestDTO.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));
        Match match = new Match();
        match.setSender(sender);
        match.setReceiver(receiver);
        match.setStatus(MatchStatus.PENDING);
        Match savedMatch = matchRepository.save(match);
        return new MatchResponse(savedMatch);
    }
    @Override
    public List<MatchResponse> getMatchesByUser(Integer userId) {
        List<Match> matches = matchRepository.findBySenderUserIdOrReceiverUserId(userId, userId);
        return matches.stream()
                .map(MatchResponse::new)
                .collect(Collectors.toList());
    }
    @Override
    public MatchResponse updateMatchStatus(Integer matchId, MatchStatus status) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        match.setStatus(status);
        Match updatedMatch = matchRepository.save(match);
        return new MatchResponse(updatedMatch);
    }

    @Override
    public void createMatchByLike(User sender, User receiver) {
        try {
            boolean checkUser1LikedUser2 = likeRepository.existsBySenderAndReceiver(sender, receiver);
            boolean checkUser2LikedUser1 = likeRepository.existsBySenderAndReceiver(receiver, sender);
            if (checkUser1LikedUser2 && checkUser2LikedUser1) {
                Match match = Match.builder()
                        .sender(sender) // set người thứ 2 like lại người thứ 1,sẽ dc coi là người tạo match (sender)
                        .receiver(receiver)
                        .matchDate(new Date())
                        .status(MatchStatus.MATCHED)
                        .build();
                matchRepository.save(match);
            }
        }catch (DataAccessException da){
            throw new DataAccessException("Can not access database");
        }
    }

    @Override
    public List<MatchBySenderResponse> getAllMatchBySenderId(int userId,int page,int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            List<Tuple> allMatchBySenderId = matchRepository.getMatchesBySenderId(userId,pageable);
            List<MatchBySenderResponse> matchBySenderResponses =  allMatchBySenderId.stream()
                    .map(matchMapper::toMatchBySenderIdResponse).toList();
            return matchBySenderResponses;
        }catch (DataAccessException de){
            throw new DataAccessException("Can not access database");
        }
    }

    @Override
    public MatchMatchIdResponse getMatchMatchId(int matchId) {
        try {
            Tuple matchById = matchRepository.getMatchByMatchId(matchId)
                    .orElseThrow(() -> new RuntimeException("Match not found"));
            return matchMapper.toMatchMatchIdResponse(matchById);
        }catch (DataAccessException da){
            throw new DataAccessException("Can not access database");
        }
    }

    @Override
    public List<UserMatchedResponse> getAllUserMatched(int userId, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            List<Tuple> allUserMatchedBySenderId = matchRepository.getFullNameAndProfileUserMatchedBySenderId(userId,pageable);
            List<UserMatchedResponse> userMatchedResponses =  allUserMatchedBySenderId.stream()
                    .map(matchMapper::toUserMatchedResponse).toList();
            return userMatchedResponses;
        }catch (DataAccessException de){
            throw new DataAccessException("Can not access database");
        }
    }

    @Override
    public void createRequestFriend(Integer senderId, Integer receiverId) {
        try {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new UserNotFoundException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new UserNotFoundException("Receiver not found"));
            Match checkMatched1 = matchRepository.findBySenderAndReceiverAndStatus(sender,receiver,MatchStatus.PENDING) ;
            Match checkMatched2 = matchRepository.findBySenderAndReceiverAndStatus(receiver,sender,MatchStatus.PENDING);
            if(checkMatched1!=null || checkMatched2!=null){
                throw new MatchAlreadyExistingException("Friend request already exists");
            }else {
                Match match = Match.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .matchDate(new Timestamp(System.currentTimeMillis()))
                        .status(MatchStatus.PENDING)
                        .build();
                matchRepository.save(match);
                notificationService.createNotificationRequestFriend(sender,receiver,MatchStatus.PENDING);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateRequestFriend(Integer senderId, Integer receiverId,MatchStatus matchStatus) {
        try {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new UserNotFoundException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new UserNotFoundException("Receiver not found"));
            Integer idMatched = matchRepository.getMatchedId(sender.getUserId(),receiver.getUserId());
            if(idMatched!=null && matchStatus.equals(MatchStatus.CANCEL)){
                matchRepository.deleteById(idMatched);
                return;
            }
            Match checkMatched1 = matchRepository.findBySenderAndReceiverAndStatus(sender,receiver,MatchStatus.PENDING) ;
            Match checkMatched2 = matchRepository.findBySenderAndReceiverAndStatus(receiver,sender,MatchStatus.PENDING);
            if(checkMatched1!=null){
                if(matchStatus.equals(MatchStatus.MATCHED)){
                    checkMatched1.setStatus(MatchStatus.MATCHED);
                    matchRepository.save(checkMatched1);
                    notificationService.createNotificationRequestFriend(sender,receiver,matchStatus);
                }else if(matchStatus.equals(MatchStatus.REJECTED)){
                    checkMatched1.setStatus(MatchStatus.REJECTED);
                    matchRepository.save(checkMatched1);
                    notificationService.createNotificationRequestFriend(sender,receiver,matchStatus);
                }else if(matchStatus.equals(MatchStatus.CANCEL)){
                    matchRepository.delete(checkMatched1);
                }
            }else
            if(checkMatched2!=null){
                if(matchStatus.equals(MatchStatus.MATCHED)){
                    checkMatched2.setStatus(MatchStatus.MATCHED);
                    matchRepository.save(checkMatched2);
                    notificationService.createNotificationRequestFriend(sender,receiver,matchStatus);
                }else if(matchStatus.equals(MatchStatus.REJECTED)){
                    checkMatched2.setStatus(MatchStatus.REJECTED);
                    matchRepository.save(checkMatched2);
                    notificationService.createNotificationRequestFriend(sender,receiver,matchStatus);
                }else if(matchStatus.equals(MatchStatus.CANCEL)){
                    matchRepository.delete(checkMatched2);
                }
            }else{
                throw new MatchNotFoundException("Match not found");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public MatchStatusResponse getMatchStatusType(Integer userId, Integer otherUserId) {
        try{
            MatchStatusResponse result = new MatchStatusResponse();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("Sender not found"));
            User other = userRepository.findById(otherUserId)
                    .orElseThrow(() -> new UserNotFoundException("Receiver not found"));
            Integer matchStatus = matchRepository.checkMatchStatus(userId,otherUserId);
            if(matchStatus==0){
                result.setMatchResponseType(MatchResponseType.UNKNOWN);
            }else if(matchStatus==1){
                result.setMatchResponseType(MatchResponseType.PENDING);
            }else if(matchStatus==2){
                result.setMatchResponseType(MatchResponseType.MATCHED);
            }
            return result;
        }catch (DataAccessException e){
            throw new DataAccessException("Cannot access database");
        }
    }
}
