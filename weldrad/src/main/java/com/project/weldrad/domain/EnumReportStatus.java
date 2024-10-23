package com.project.weldrad.domain;

public enum EnumReportStatus {
    PENDENTE("Radiografia com análise pendente"),
    APROVADA("Radiografia aprovada"),
    REPROVADA("Radiografia reprovada");

    private String description;

    EnumReportStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
