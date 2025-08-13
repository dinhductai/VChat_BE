package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.ReportTypeRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.ReportTypeResponse;
import com.website.loveconnect.entity.ReportType;
import com.website.loveconnect.service.ReportTypeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://127.0.0.1:5500")  chi ap dung local
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminReportTypeController {
    ReportTypeService reportTypeService;

    @PostMapping(value = "/report-types")
    public ResponseEntity<ApiResponse<String>> createReportType(@RequestBody ReportTypeRequest newReportType) {
        reportTypeService.createReport(newReportType);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,
                "Create new report type successfull",null));
    }

    @PutMapping(value = "/report-types/{idReportType}")
    public ResponseEntity<ApiResponse<String>> updateReportType(
            @PathVariable int idReportType,
            @RequestBody ReportTypeRequest reportTypeUpdate) {
        reportTypeService.updateReport(idReportType,reportTypeUpdate);
        return ResponseEntity.ok(new ApiResponse<>(true, "Update report type successfull",null));
    }

    @DeleteMapping(value = "/report-types/{idReportType}")
    public ResponseEntity<Void> deleteReportType(@PathVariable int idReportType) {
        reportTypeService.deleteReport(idReportType);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/report-types")
    public ResponseEntity<ApiResponse<List<ReportTypeResponse>>> getAllReportTypes() {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all report types",
                reportTypeService.getAllReportType()));
    }
}
