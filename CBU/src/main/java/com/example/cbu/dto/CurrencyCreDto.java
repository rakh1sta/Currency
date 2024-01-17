package com.example.cbu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class CurrencyCreDto {
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
        @NotBlank(message = "English name of currency cant be blank")
        @NotNull(message = "English name of currency cant be null")
        @JsonProperty("CcyNm_EN")
        String nameEnglish;
        @NotBlank(message = "Uzbek name of currency cant be blank")
        @NotNull(message = "Uzbek name of currency cant be null")
        @JsonProperty("CcyNm_UZ")
        String nameUzbek;
        @NotBlank(message = "Uzbek-Krill name of currency cant be blank")
        @NotNull(message = "Uzbek-Krill name of currency cant be null")
        @JsonProperty("CcyNm_UZC")
        String nameUzbekKrill;
        @NotBlank(message = "Russian name of currency cant be blank")
        @NotNull(message = "Russian name of currency cant be null")
        @JsonProperty("CcyNm_RU")
        String nameRussian;
        @NotBlank(message = "nominal cant be blank")
        @NotNull(message = "nominal cant be null")
        @Pattern(regexp = "[1|10]", message = "should be 1 or 10")
        @JsonProperty("Nominal")
        String nominal;
        @NotBlank(message = "rate cant be blank")
        @NotNull(message = "rate cant be null")
        @JsonProperty("Rate")
        String rate;
        @NotBlank(message = "Differance cant be blank")
        @NotNull(message = "Difference cant be null")
        @JsonProperty("Diff")
        String difference;
        @NotBlank(message = "Date cant be blank")
        @NotNull(message = "Date cant be null")
        @Pattern(regexp = "^([1-9]|0[1-9]|[12][0-9]|3[0-1])\\.([1-9]|0[1-9]|1[0-2])\\.\\d{4}$", message = "wrong format")
        @JsonProperty("Date")
        String date;
}
