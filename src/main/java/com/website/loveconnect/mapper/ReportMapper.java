package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.ReportResponse;
import com.website.loveconnect.enumpackage.StatusReport;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.sql.Date;

@Component
public class ReportMapper {
    public ReportResponse toReportResponse(Tuple tuple) {
        return ReportResponse.builder()
                .reportId(tuple.get("reportId", Integer.class))
                .reportDate(tuple.get("reportDate", Timestamp.class))
                .reportStatus(tuple.get("reportStatus", String.class) != null ?
                        StatusReport.valueOf(tuple.get("reportStatus", String.class)) : null)
                .detail(tuple.get("detail", String.class))
                .reviewDate(tuple.get("reviewDate", Timestamp.class))
                .reporterId(tuple.get("reporterId", Integer.class))
                .reporterFullName(tuple.get("reporterFullName", String.class))
                .reportedId(tuple.get("reportedId", Integer.class))
                .reportedFullName(tuple.get("reportedFullName", String.class))
                .reportTypeName(tuple.get("reportTypeName", String.class))
                .reviewerFullName(tuple.get("reviewerFullName", String.class))
                .build();
    }
}
