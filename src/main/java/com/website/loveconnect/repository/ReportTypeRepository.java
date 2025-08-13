package com.website.loveconnect.repository;

import com.website.loveconnect.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {
    boolean existsByTypeName(String typeName);
    ReportType findByTypeName(String typeName);

}
