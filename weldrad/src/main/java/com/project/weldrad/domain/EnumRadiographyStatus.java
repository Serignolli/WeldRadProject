package com.project.weldrad.domain;

public enum EnumRadiographyStatus {
    PENDENTE("Radiografia com análise pendente", EnumReportStatus.PENDENTE),
    APROVADA("Radiografia aprovada", EnumReportStatus.APROVADA),
    REPROVADA("Radiografia reprovada", EnumReportStatus.REPROVADA);

    private final String description;
    private final EnumReportStatus reportStatus;

    EnumRadiographyStatus(String description, EnumReportStatus reportStatus) {
        this.description = description;
        this.reportStatus = reportStatus;
    }

    public String getDescription() {
        return description;
    }

    public EnumReportStatus toReportStatus() {
        return reportStatus;
    }
}
