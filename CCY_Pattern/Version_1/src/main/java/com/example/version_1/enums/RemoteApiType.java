package com.example.version_1.enums;

import lombok.Getter;

import java.util.Arrays;

public enum RemoteApiType {
    CBU("https://cbu.uz/ru/arkhiv-kursov-valyut/json/","cbu,cbu_ccy,cbu_currency"),
    NBU("https://nbu.uz/en/exchange-rates/json/","nbu,nbu_ccy,nbu_currency");
    @Getter
    private final String url;
    private final String names;

    RemoteApiType(String url, String names) {
        this.url = url;
        this.names = names;
    }
    public static boolean checkRemoteApiType(String type, String name) {
        return Arrays.stream(values())
                .anyMatch(value -> value.name().equals(type)
                        && value.names.contains(name));
    }
}
