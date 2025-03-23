package com.website.loveconnect.dto.response;

import com.website.loveconnect.entity.Match;
import com.website.loveconnect.enumpackage.MatchStatus;
import lombok.Data;

import java.util.Date;

@Data
public class MatchResponse {
    private Integer matchId;
    private Integer senderId;
    private Integer receiverId;
    private Date matchDate;
    private MatchStatus status;

    public MatchResponse(Match match) {
        this.matchId = match.getMatchId();
        this.senderId = match.getSender().getId();
        this.receiverId = match.getReceiver().getId();
        this.matchDate = match.getMatchDate();
        this.status = match.getStatus();
    }
}
