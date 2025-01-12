package com.project.weldrad.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RadiographyListDTO {
    private Long id;
    private String fileName;
    private String submissionDate;
    private String submissionUser;
    private String status;
    private String description;
    private String material;
    private int thickness;
    private int diameter;
}

