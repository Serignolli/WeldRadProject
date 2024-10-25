package com.project.weldrad.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REPORT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "archive_name", nullable = false, length = 50)
    private String archive_name;

    @Column(name = "archive", nullable = false, length = 255)
    private String archive;

    @Column(name = "submission_date", nullable = false)
    private LocalDateTime submission_date;

    @Column(name = "submission_user", nullable = false, length = 50)
    private String submission_user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnumReportStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EnumType type;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "material", nullable = false, length = 55)
    private String material;

    @Column(name = "thickness", nullable = false)
    private int thickness;

    @Column(name = "diameter", nullable = false)
    private int diameter;

    //Relacionamento com a tabela Radiography
    @OneToOne
    @JoinColumn(name = "radiography_id", referencedColumnName = "id")
    private Radiography radiography;
}
