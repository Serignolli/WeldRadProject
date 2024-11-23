package com.project.weldrad.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.weldrad.domain.Radiography;
import com.project.weldrad.domain.Report;
import com.project.weldrad.repository.RadiographyRepository;
import com.project.weldrad.repository.ReportRepository;

import jakarta.servlet.http.HttpServletRequest;

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
        pdfGenerator.generatePDF(report, reportRepository);
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

    public ResponseEntity<Resource> downloadReport(Long id, HttpServletRequest request) throws IOException {
        Report rep = reportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Laudo não encontrado"));
    
        String baseDirectory = System.getProperty("user.dir");
        Path basePath = Paths.get(baseDirectory);
        Path absolutePath = basePath.resolve(rep.getFilePath()).normalize().toAbsolutePath();
    
        if (!absolutePath.startsWith(baseDirectory) || !Files.exists(absolutePath)) {
            return ResponseEntity.notFound().build();
        }
    
        Resource resource = new UrlResource(absolutePath.toUri());
        String contentType = Files.probeContentType(absolutePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
    
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            .header(HttpHeaders.PRAGMA, "no-cache")
            .header(HttpHeaders.EXPIRES, "0")
            .body(resource);
    }
}
