package com.example.version_2.dto.nbu;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class NbuCcyRecuestDto {
    @JsonProperty("title")
    private String title;
    @NotNull
    @NotBlank
    @JsonProperty("code")
    private String currency;
    @NotNull
    @JsonProperty("cb_price")
    private BigDecimal rate;
    @JsonProperty("nbu_buy_price")
    private BigDecimal buyPrice;
    @JsonProperty("nbu_cell_price")
    private BigDecimal sellPrice;
    @JsonProperty("date")
    @Pattern(regexp = "dd/MM/yyyy")
    private String date;
}
