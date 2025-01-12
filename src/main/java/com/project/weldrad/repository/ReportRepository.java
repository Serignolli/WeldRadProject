package com.project.weldrad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.weldrad.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}