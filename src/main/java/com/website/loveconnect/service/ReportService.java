package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.ReportRequest;
import com.website.loveconnect.dto.request.ReportTypeRequest;
import com.website.loveconnect.entity.Report;
import com.website.loveconnect.entity.ReportType;
import com.website.loveconnect.enumpackage.StatusReport;

public interface ReportService {
    void createReport(String typeName, ReportRequest reportRequest);
}
