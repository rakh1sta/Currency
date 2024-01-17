package com.example.cbu_caching.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyDto {
    private String code;
    private String currency;
    private String nameEnglish;
    private String nameUzbek;
    private String nameUzbekKrill;
    private String nameRussian;
    private String nominal;
    private String rate;
    private String difference;
    private String date;
}
