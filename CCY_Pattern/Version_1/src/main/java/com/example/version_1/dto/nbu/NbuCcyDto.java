package com.example.version_1.dto.nbu;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NbuCcyDto {
    private String title;
    private String currency;
    private BigDecimal rate;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private String date;
}
