package com.project.weldrad.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.weldrad.domain.Radiography;
import com.project.weldrad.domain.Report;
import com.project.weldrad.repository.RadiographyRepository;
import com.project.weldrad.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final RadiographyRepository radiographyRepository;

    //Constructor
    public ReportService(ReportRepository reportRepository, RadiographyRepository radiographyRepository) {
        this.reportRepository = reportRepository;
        this.radiographyRepository = radiographyRepository;
    }
    
    public void createPDF(Long radId) throws IOException {
        Report report = this.saveReport(radId);

        PdfGenerator pdfGenerator = new PdfGenerator();
        pdfGenerator.generatePDF(report);
    }

    private Report saveReport(Long radId) {
        Radiography rad = radiographyRepository.findById(radId)
                        .orElseThrow(() -> new RuntimeException("Radiografia não encontrada"));
        Report rep = new Report();

        rep.setFileName(rad.getFileName());
        rep.setFilePath(rad.getFilePath());
        rep.setSubmissionDate(LocalDateTime.now());
        rep.setSubmissionUser("Jorginho");
        rep.setStatus(rad.getStatus().toReportStatus());
        rep.setDescription(rad.getDescription());
        rep.setMaterial(rad.getMaterial());
        rep.setThickness(rad.getThickness());
        rep.setDiameter(rad.getDiameter());
        rep.setRadiography(rad);
        rad.setReport(rep);

        reportRepository.save(rep);
        radiographyRepository.save(rad);

        return rep;
    }
}
