package com.project.weldrad.domain;

public enum EnumRadiographyStatus {
    PENDENTE("Radiografia com análise pendente"),
    APROVADA("Radiografia aprovada"),
    REPROVADA("Radiografia reprovada");

    private String description;

    EnumRadiographyStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
