package com.example.version_1.dto.cbu;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CbuCcyDto {
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
