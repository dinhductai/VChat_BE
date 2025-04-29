package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.ReportRequest;
import com.website.loveconnect.dto.request.ReportTypeRequest;
import com.website.loveconnect.dto.request.ReportUpdateStatusRequest;
import com.website.loveconnect.dto.response.ReportResponse;
import com.website.loveconnect.entity.Report;
import com.website.loveconnect.entity.ReportType;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.StatusReport;
import com.website.loveconnect.exception.*;
import com.website.loveconnect.mapper.ReportMapper;
import com.website.loveconnect.repository.ReportRepository;
import com.website.loveconnect.repository.ReportTypeRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.ReportService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ReportServiceImpl implements ReportService {
    ReportRepository reportRepository;
    UserRepository userRepository;
    ReportTypeRepository reportTypeRepository;
    ReportMapper reportMapper;

    @Override
    public void createReport(String typeName, ReportRequest reportRequest) {
        try{
            User reporter = userRepository.findById(reportRequest.getReporterId())
                    .orElseThrow(()->new UserNotFoundException("Reporter not found"));
            User reported = userRepository.findById(reportRequest.getReportedId())
                    .orElseThrow(()->new UserNotFoundException("Reported not found"));
            boolean checkReportExisted = reportRepository
                    .existsByReporterAndReportedAndStatusReport(reporter,reported,StatusReport.PENDING);
            boolean checkReportTypeExisted = reportTypeRepository.existsByTypeName(typeName);
            if(checkReportExisted ){
                throw new ReportConflictedException("Report already exists, wait admin review");
            }else if(!checkReportTypeExisted ){
                throw new ReportTypeNotFoundException("Report type not exists, wait admin review");
            }else {
                ReportType reportType = reportTypeRepository.findByTypeName(typeName);
                Report report = Report.builder()
                        .reporter(reporter)
                        .reported(reported)
                        .reportType(reportType)
                        .additionalDetails(reportRequest.getAdditionalDetails())
                        .reportDate(reportRequest.getReportDate())
                        .statusReport(StatusReport.PENDING)
                        .build();
                reportRepository.save(report);
            }
        }catch (DataAccessException de){
            throw new DataAccessException("Can not access database");
        }
    }

    @Override
    public void updateStatusReport(ReportUpdateStatusRequest reportUpdateStatusRequest) {
        try {
            Report report = reportRepository.findById(reportUpdateStatusRequest.getReportId())
                    .orElseThrow(()-> new ReportNotFoundException("Report not found"));
            report.setStatusReport(reportUpdateStatusRequest.getStatusReport());
            reportRepository.save(report);
        }catch (DataAccessException da){
            throw new DataAccessException("Can not access database");
        }
    }

    @Override
    public List<ReportResponse> getAllReports() {
        try{
            return reportRepository.findAllReport()
                    .stream().map(reportMapper::toReportResponse).toList();
        }catch (DataAccessException da){
            throw new DataAccessException("Can not access database");
        }
    }
}
