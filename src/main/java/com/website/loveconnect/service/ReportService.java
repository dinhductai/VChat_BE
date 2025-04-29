package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.ReportRequest;
import com.website.loveconnect.dto.request.ReportTypeRequest;
import com.website.loveconnect.dto.request.ReportUpdateStatusRequest;
import com.website.loveconnect.dto.response.ReportResponse;
import com.website.loveconnect.entity.Report;
import com.website.loveconnect.entity.ReportType;
import com.website.loveconnect.enumpackage.StatusReport;

import java.util.List;

public interface ReportService {
    void createReport(String typeName, ReportRequest reportRequest);
    void updateStatusReport(ReportUpdateStatusRequest reportUpdateStatusRequest);
    List<ReportResponse> getAllReports();
}
