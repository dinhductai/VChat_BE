package com.website.loveconnect.dto.response;

import com.website.loveconnect.entity.Match;
import com.website.loveconnect.enumpackage.MatchStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {
    private Integer matchId;
    private Integer senderId;
    private Integer receiverId;
    private Date matchDate;
    private MatchStatus status;

    public MatchResponse(Match match) {
        this.matchId = match.getMatchId();
        this.senderId = match.getSender().getUserId();
        this.receiverId = match.getReceiver().getUserId();
        this.matchDate = match.getMatchDate();
        this.status = match.getStatus();
    }
}
