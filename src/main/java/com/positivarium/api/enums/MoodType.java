package com.positivarium.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MoodType {
    HAPPY("Heureux", "#FFFF00"),
    SAD("Triste", "#0000FF"),
    ANGRY("Fâché", "#FF0000");

    private final String label;
    private final String color;
}
