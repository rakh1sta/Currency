package com.example.version_2.dto.cbu;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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
public class CbuCcyRecuestDto {
    @Min(value = 1, message = "Id cant be less than 1")
    Integer id;
    @Pattern(regexp = "[0-9]{3}", message = "wrong format")
    @NotBlank(message = "code cant be blank")
    @NotNull(message = "code cant be null")
    @JsonProperty("Code")
    String code;
    @Pattern(regexp = "[A-Z]{3}", message = "wrong format")
    @NotBlank(message = "currency cant be blank")
    @NotNull(message = "currency cant be null")
    @JsonProperty("Ccy")
    String currency;
    @JsonProperty("CcyNm_EN")
    String nameEnglish;
    @JsonProperty("CcyNm_UZ")
    String nameUzbek;
    @JsonProperty("CcyNm_UZC")
    String nameUzbekKrill;
    @JsonProperty("CcyNm_RU")
    String nameRussian;
    @JsonProperty("Nominal")
    String nominal;
    @JsonProperty("Rate")
    BigDecimal rate;
    @NotBlank(message = "Differance cant be blank")
    @NotNull(message = "Difference cant be null")
    @JsonProperty("Diff")
    String difference;
    @JsonProperty("Date")
    String date;
}
