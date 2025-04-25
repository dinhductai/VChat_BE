package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.MatchBySenderResponse;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.enumpackage.MatchStatus;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
public class MatchMapper {
    public MatchBySenderResponse toMatchBySenderIdResponse(Tuple matchTuple) {
        return MatchBySenderResponse.builder()
                .matchDate(matchTuple.get("matchDate", Timestamp.class))
                .matchStatus(MatchStatus.valueOf(matchTuple.get("status", String.class)))
                .fullName(matchTuple.get("fullName", String.class))
                .gender(Gender.valueOf(matchTuple.get("gender", String.class)))
                .description(matchTuple.get("description", String.class))
                .build();
    }
}
