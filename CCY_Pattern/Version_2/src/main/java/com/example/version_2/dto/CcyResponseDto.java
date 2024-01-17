package com.example.version_2.dto;


import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CcyResponseDto {
    private String title;
    private String currency;
    private BigDecimal rate;
    private String date;
}
