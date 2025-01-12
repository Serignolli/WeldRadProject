package com.project.weldrad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.weldrad.domain.Radiography;

public interface RadiographyRepository extends JpaRepository<Radiography, Long> {

}