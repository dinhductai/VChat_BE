package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.ReportTypeRequest;
import com.website.loveconnect.dto.response.ReportTypeResponse;
import com.website.loveconnect.entity.ReportType;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.ReportTypeConflictedException;
import com.website.loveconnect.exception.ReportTypeNotFoundException;
import com.website.loveconnect.mapper.ReportTypeMapper;
import com.website.loveconnect.repository.ReportTypeRepository;
import com.website.loveconnect.service.ReportTypeService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ReportTypeServiceImpl implements ReportTypeService {
    ReportTypeRepository reportTypeRepository;
    ReportTypeMapper reportTypeMapper;
    @Override
    public void createReport(ReportTypeRequest newReportType) {
        try {
            boolean existingReportName = false;
            existingReportName = reportTypeRepository.existsByTypeName(newReportType.getTypeName());
            if (!existingReportName) {
                ReportType reportType = ReportType.builder()
                        .typeName(newReportType.getTypeName())
                        .description(newReportType.getDescription())
                        .build();
                reportTypeRepository.save(reportType);
            }
            else{
                throw new ReportTypeConflictedException("Report type already exists");
            }
        }catch (DataAccessException de){
            throw new DataAccessException("Cannot access to database");
        }
    }

    @Override
    public void updateReport(int idReportType,ReportTypeRequest reportTypeUpdate) {
        try {
            if (idReportType>0) {
                ReportType reportType = reportTypeRepository.findById(idReportType)
                        .orElseThrow(()-> new ReportTypeNotFoundException("Report type not found"));
                reportType.setTypeName(reportTypeUpdate.getTypeName());
                reportType.setDescription(reportTypeUpdate.getDescription());
                reportTypeRepository.save(reportType);
            }
            else{
                throw new IllegalArgumentException("Id report type can not < 0");
            }
        }catch (DataAccessException de){
            throw new DataAccessException("Cannot access to database");
        }
    }

    @Override
    public void deleteReport(int idReportType) {
        try{
            if (idReportType>0) {
                ReportType reportType = reportTypeRepository.findById(idReportType)
                        .orElseThrow(()-> new ReportTypeNotFoundException("Report type not found"));
                reportTypeRepository.delete(reportType);
            }else{
                throw new IllegalArgumentException("Id report type can not < 0");
            }
        }catch (DataAccessException de){
            throw new DataAccessException("Cannot access to database");
        }

    }

    @Override
    public List<ReportTypeResponse> getAllReportType() {
        try{
            return reportTypeRepository.findAll()
                    .stream().map(reportTypeMapper::toReportTypeResponse).toList();

        }catch (DataAccessException de){
            throw new DataAccessException("Cannot access to database");
        }
    }
}
