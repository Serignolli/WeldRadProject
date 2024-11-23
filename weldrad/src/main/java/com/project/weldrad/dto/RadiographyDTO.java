package com.project.weldrad.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RadiographyDTO {
    private String description;

    private String material;

    private int thickness;

    private int diameter;
}
