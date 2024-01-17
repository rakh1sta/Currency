package com.example.version_2.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum RateType {

    CBU("https://cbu.uz/ru/arkhiv-kursov-valyut/json/", "cbu,cbu_ccy,cbu_currency"),
    NBU("https://nbu.uz/en/exchange-rates/json/", "nbu,nbu_ccy,nbu_currency"),
    ALL("", "*");

    private final String url;
    private final List<String> names;

    RateType(String url, String names) {
        this.url = url;
        this.names = Arrays.asList(names.split(","));
    }
}
