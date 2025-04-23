package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.MatchRequestDTO;
import com.website.loveconnect.dto.response.MatchResponse;
import com.website.loveconnect.enumpackage.MatchStatus;

import java.util.List;

public interface MatchService {
    MatchResponse createMatch(MatchRequestDTO matchRequestDTO);
    List<MatchResponse> getMatchesByUser(Integer userId);
    MatchResponse updateMatchStatus(Integer matchId, MatchStatus status);
    void createMatchByLike(int userId1, int userId2);
}
