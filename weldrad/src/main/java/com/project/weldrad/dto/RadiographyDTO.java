package com.project.weldrad.dto;

import com.project.weldrad.domain.EnumType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RadiographyDTO {
    private EnumType type;

    private String description;

    private String material;

    private int thickness;

    private int diameter;
}
