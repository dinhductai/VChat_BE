package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.MatchRequestDTO;
import com.website.loveconnect.dto.response.MatchResponse;
import com.website.loveconnect.entity.Match;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.LikeRepository;
import com.website.loveconnect.repository.MatchRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public void createMatchByLike(int userId1, int userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));
        boolean checkUser1LikedUser2 = likeRepository.existsBySenderAndReceiver(user1,user2);
        boolean checkUser2LikedUser1 = likeRepository.existsBySenderAndReceiver(user2,user1);
        if (checkUser1LikedUser2 && checkUser2LikedUser1) {
            Match match = Match.builder()
                    .sender(user1)
                    .receiver(user2)
                    .matchDate(new Date())
                    .status(MatchStatus.MATCHED)
                    .build();
            matchRepository.save(match);
        }
        else throw new RuntimeException();

    }
}
