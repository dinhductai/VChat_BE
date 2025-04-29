package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.ReportUpdateStatusRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.ReportResponse;
import com.website.loveconnect.entity.Report;
import com.website.loveconnect.service.ReportService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminReportController {
    ReportService reportService;

    @PostMapping(value = "/reports/update")
    public ResponseEntity<ApiResponse<String>> updateReport(
            @RequestBody ReportUpdateStatusRequest reportUpdateStatusRequest) {
        reportService.updateStatusReport(reportUpdateStatusRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Update report status successful",null));
    }

    @GetMapping(value = "/reports")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getAllReports() {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all report successful",
                reportService.getAllReports()));
    }


}
