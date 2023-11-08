package ru.garrowd.admissionservice.utils.enums;

public enum ApplicationStatuses {
    PENDING("PENDING"),
    WAITING("WAITING"),
    APPROVED("APPROVED"),
    REJECTED_BY_SUPERVISOR("REJECTED_BY_SUPERVISOR"),
    REJECTED_BY_ADMISSION("REJECTED_BY_ADMISSION");

    private final String value;

    ApplicationStatuses(String value) {
        this.value = value;
    }
}
