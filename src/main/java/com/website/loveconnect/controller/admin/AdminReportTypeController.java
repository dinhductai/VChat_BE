package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.ReportTypeRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.entity.ReportType;
import com.website.loveconnect.service.ReportTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://127.0.0.1:5500")  chi ap dung local
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminReportTypeController {
    ReportTypeService reportTypeService;

    @PostMapping(value = "/create-reporttype")
    public ResponseEntity<ApiResponse<String>> createReportType(@RequestBody ReportTypeRequest newReportType) {
        reportTypeService.createReport(newReportType);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,
                "Create new report type successfull",null));
    }

}
