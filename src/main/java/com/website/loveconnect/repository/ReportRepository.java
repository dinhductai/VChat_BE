package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Report;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.StatusReport;
import com.website.loveconnect.repository.query.ReportQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    boolean existsByReporterAndReportedAndStatusReport(User reporter,User reported, StatusReport statusReport);

    @Query(value = ReportQueries.FIND_ALL_REPORT,nativeQuery = true)
    List<Tuple> findAllReport();

    Optional<Report> findFirstByReported(User reported);



}
