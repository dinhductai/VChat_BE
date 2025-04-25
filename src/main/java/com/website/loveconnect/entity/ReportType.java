package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "report_types")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_type_id")
    private Integer reportTypeId;

    @Column(name = "type_name",nullable = false,unique = true)
    private String typeName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "reportType",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();
}
