package com.assessment.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ResponseStatus {
    SUCCESS, FAIL, ERROR;

    /**
     * Creator method
     */
    @JsonCreator
    public static ResponseStatus from(String value) {
        return Arrays.asList(ResponseStatus.values()).stream()
            .filter(item -> item.toValue().equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Value method
     */
    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

}
