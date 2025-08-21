package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.StatusReport;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private int reportId;
    private Timestamp reportDate;
    private StatusReport reportStatus;
    private String detail;
    private Timestamp reviewDate;
    private Integer reporterId;
    private String reporterFullName;
    private Integer reportedId;
    private String reportedFullName;
    private String reportTypeName;
    private String reviewerFullName;
}
