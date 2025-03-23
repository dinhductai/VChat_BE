package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.MatchRequestDTO;
import com.website.loveconnect.dto.response.MatchResponse;
import com.website.loveconnect.entity.Match;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.MatchRepository;
import com.website.loveconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public MatchService(MatchRepository matchRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

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

    public List<MatchResponse> getMatchesByUser(Integer userId) {
        List<Match> matches = matchRepository.findBySenderIdOrReceiverId(userId, userId);
        return matches.stream()
                .map(MatchResponse::new)
                .collect(Collectors.toList());
    }

    public MatchResponse updateMatchStatus(Integer matchId, MatchStatus status) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        match.setStatus(status);
        Match updatedMatch = matchRepository.save(match);
        return new MatchResponse(updatedMatch);
    }
}
