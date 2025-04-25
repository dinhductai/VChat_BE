package com.website.loveconnect.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeResponse {
    private Integer reportTypeId;
    private String typeName;
    private String description;

}
