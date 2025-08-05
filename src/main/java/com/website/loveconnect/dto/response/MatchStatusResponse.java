package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.MatchResponseType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchStatusResponse {
    private MatchResponseType matchResponseType;

}
