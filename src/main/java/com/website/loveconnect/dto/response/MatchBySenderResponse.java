package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.enumpackage.MatchStatus;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchBySenderResponse {
    private Integer matchId;
    private Timestamp matchDate;
    private MatchStatus matchStatus;
    private Integer receiverId;
    private String fullName;
    private Gender gender;
    private String description;
    private String photoUrl;

}
