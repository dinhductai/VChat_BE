package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.MatchRequestDTO;
import com.website.loveconnect.dto.response.MatchResponse;
import com.website.loveconnect.entity.Match;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.MatchRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
