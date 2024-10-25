package com.project.weldrad.domain;

public enum EnumReportStatus {
    PENDENTE("Laudo com análise pendente"),
    APROVADA("Laudo aprovado"),
    REPROVADA("Laudo reprovado");

    private String description;

    EnumReportStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
