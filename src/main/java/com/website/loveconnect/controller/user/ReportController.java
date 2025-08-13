package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.ReportRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.ReportTypeResponse;
import com.website.loveconnect.entity.Report;
import com.website.loveconnect.service.ReportService;
import com.website.loveconnect.service.ReportTypeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class ReportController {
    ReportService reportService;
    ReportTypeService reportTypeService;

    @Operation(summary = "Report user", description = "User make a report to other user")
    @PostMapping(value = "/report")
    public ResponseEntity<ApiResponse<String>> report(
            @RequestParam(value = "typeName") String typeName,
            @RequestBody ReportRequest reportRequest) {
        reportService.createReport(typeName, reportRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create report successful",null));
    }

    @Operation(summary = "Get report types",description = "Get all report type for user")
    @GetMapping(value = "/report-types")
    public ResponseEntity<ApiResponse<List<ReportTypeResponse>>> getAllReportTypes() {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all report types",
                reportTypeService.getAllReportType()));
    }

}
