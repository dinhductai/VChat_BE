package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.ReportTypeResponse;
import com.website.loveconnect.entity.ReportType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportTypeMapper {
    public ReportTypeResponse toReportTypeResponse(ReportType reportType) {
        return ReportTypeResponse.builder()
                .reportTypeId(reportType.getReportTypeId())
                .typeName(reportType.getTypeName())
                .description(reportType.getDescription())
                .build();
    }
}
