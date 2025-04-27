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
public class MatchMatchIdResponse {
    private Timestamp matchDate;
    private MatchStatus matchStatus;
    private String fullName;
    private Gender gender;
    private String description;

}
