package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.ReportUpdateStatusRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.entity.Report;
import com.website.loveconnect.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminReportController {
    ReportService reportService;

    @PostMapping(value = "/report/update")
    public ResponseEntity<ApiResponse<String>> updateReport(
            @RequestBody ReportUpdateStatusRequest reportUpdateStatusRequest) {
        reportService.updateStatusReport(reportUpdateStatusRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Update report status successful",null));
    }



}
