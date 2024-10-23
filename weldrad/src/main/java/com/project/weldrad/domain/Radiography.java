package com.project.weldrad.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RADIOGRAPHY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Radiography {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "archive_name", nullable = false, length = 50)
    private String archive_name;

    //BLOB - Mais facilidade?
    //@Column(name = "archive", nullable = false)
    //private byte[] archive;

    //Caminho do arquivo - Mais desempenho?
    @Column(name = "archive", nullable = false, length = 255)
    private String archive;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnumRadiographyStatus status;

    @Column(name = "submission_date", nullable = false)
    private LocalDateTime submission_date;

    @Column(name = "submission_user", nullable = false, length = 50)
    private String submission_user;

    //Relacionamento com a tabela Report
    @OneToOne(mappedBy = "radiography")
    private Report report;
}
