package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.ReportTypeRequest;
import com.website.loveconnect.dto.response.ReportTypeResponse;
import com.website.loveconnect.entity.Report;
import com.website.loveconnect.entity.ReportType;

import java.util.List;

public interface ReportTypeService {
    void createReport(ReportTypeRequest newReportType);
    void updateReport(int idReportType,ReportTypeRequest reportTypeUpdate);
    void deleteReport(int idReportType);
    List<ReportTypeResponse> getAllReportType();
}
