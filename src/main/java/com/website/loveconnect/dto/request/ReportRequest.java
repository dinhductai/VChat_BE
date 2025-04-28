package com.website.loveconnect.dto.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    private int reporterId;
    private int reportedId;
    private String additionalDetails;
    private Timestamp reportDate = new Timestamp(System.currentTimeMillis());

}
