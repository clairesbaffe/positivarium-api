package com.positivarium.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublisherRequestStatusEnum {
    PENDING("En attente"),
    UNDER_REVIEW("En révision"),
    APPROVED("Approuvée"),
    REJECTED("Refusée"),
    CANCELLED("Annulée");

    private final String label;
}
