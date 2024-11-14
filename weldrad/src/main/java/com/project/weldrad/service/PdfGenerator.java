package com.project.weldrad.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.project.weldrad.domain.Report;

public class PdfGenerator {

    public void generatePDF(Report rep) throws IOException {

        File reportsDir = new File("reportsDir");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        String outputFilePath = "reportsDir/" + rep.getFileName();

        //Path absolutePath = new File(outputFilePath).toPath().toAbsolutePath();

        PdfWriter writer = new PdfWriter(new FileOutputStream(outputFilePath));
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);

        //Fonts
        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        //Titles
        Paragraph title1 = new Paragraph("CENTRO UNIVERSITÁRIO SENAC").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(title1);

        Paragraph title2 = new Paragraph("MORDEDORES DE FRONHA").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(title2);

        Paragraph title3 = new Paragraph("LAUDO DA ANÁLISE RADIOGRÁFICA").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(title3);

        document.add(new Paragraph("\n"));

        // Profissional information
        document.add(new Paragraph("Responsável: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getSubmissionUser()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("Nome: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getSubmissionUser()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("Data: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(rep.getSubmissionDate().toString()).setFont(normalFont).setFontSize(12));

        document.add(new Paragraph("\n"));

        //Radiography informations
        document.add(new Paragraph("Identificação da radiografia: ").setFont(titleFont).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
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
