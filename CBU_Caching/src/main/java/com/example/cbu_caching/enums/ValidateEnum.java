package com.example.cbu_caching.enums;

import lombok.Getter;

@Getter
public enum ValidateEnum {
    NOT_NULL("can not be null"),
    NOT_BLANK("can not be blank"),
    MIN("can not be less than"),
    MAX("can not me greater than"),
    PATTERN("not acceptable format");

    private final String val;

    ValidateEnum(String val) {
        this.val = val;
    }
}
