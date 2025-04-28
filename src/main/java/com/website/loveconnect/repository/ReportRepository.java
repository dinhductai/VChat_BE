package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Report;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.StatusReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    boolean existsByReporterAndReportedAndStatusReport(User reporter,User reported, StatusReport statusReport);
}
