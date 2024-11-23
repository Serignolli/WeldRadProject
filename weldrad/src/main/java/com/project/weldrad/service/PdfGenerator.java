package com.project.weldrad.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.project.weldrad.domain.Report;
import com.project.weldrad.repository.ReportRepository;

public class PdfGenerator {

    public void generatePDF(Report rep, ReportRepository reportRepository) throws IOException {

        File reportsDir = new File("reportsDir");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        String originalFileName = rep.getFileName();
        String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String pdfFileName = fileNameWithoutExtension + ".pdf";

        String outputFilePath = "reportsDir/" + pdfFileName;

        String baseDirectory = System.getProperty("user.dir");

        Path absolutePath = Paths.get(baseDirectory, outputFilePath).toAbsolutePath();

        rep.setFilePath(absolutePath.toString());

        rep.setFileName(pdfFileName);
        rep.setFilePath(absolutePath.toString());

        reportRepository.save(rep);

        PdfWriter writer = new PdfWriter(new FileOutputStream(outputFilePath));
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);

        //Fonts
        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        //Titles
        Paragraph title1 = new Paragraph("CENTRO UNIVERSITÁRIO SENAC").setFont(titleFont).setFontSize(16).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(title1);

        Paragraph title2 = new Paragraph("WELDRAD").setFont(titleFont).setFontSize(16).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(title2);

        Paragraph title3 = new Paragraph("LAUDO DA ANÁLISE RADIOGRÁFICA").setFont(titleFont).setFontSize(16).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(title3);

        document.add(new Paragraph("\n"));

        // Profissional information
        document.add(new Paragraph("Responsável ").setFont(titleFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT));

        document.add(new Paragraph("Nome: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getSubmissionUser()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("Data: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getSubmissionDate().toString()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("\n"));

        //Radiography informations
        document.add(new Paragraph("Identificação da radiografia ").setFont(titleFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getFileName()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("Material: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getMaterial()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("Diâmetro: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(String.valueOf(rep.getDiameter())).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("Espessura: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(String.valueOf(rep.getThickness())).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("Descrição: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getDescription()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("\n"));

        //Result
        Paragraph resultTitle = new Paragraph("RESULTADO").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(resultTitle);

        document.add(new Paragraph(rep.getStatus().getDescription()).setFont(normalFont).setFontSize(12));

        document.close();
    }
}
