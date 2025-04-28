package com.website.loveconnect.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.StatusReport;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Table(name = "reports")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "additional_details")
    private String additionalDetails;

    @Column(name = "report_date")
    private Timestamp reportDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "status_report")
    @Enumerated(EnumType.STRING)
    private StatusReport statusReport = StatusReport.PENDING;

    @Column(name = "review_date")
    private Timestamp reviewDate = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reporter_id",nullable = false)
    private User reporter; // người báo cáo

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reported_id",nullable = false)
    private User reported; // người bị báo cáo

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "report_type_id",nullable = false)
    private ReportType reportType; // loại báo cáo

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reviewed_by")
    private User reviewer; // người check báo cáo


}
