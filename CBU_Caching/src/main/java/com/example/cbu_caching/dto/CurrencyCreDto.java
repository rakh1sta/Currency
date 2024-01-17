package com.example.cbu_caching.dto;

import com.example.cbu_caching.annatation.Validator;
import com.example.cbu_caching.enums.ValidateEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;

import static com.example.cbu_caching.enums.ValidateEnum.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class CurrencyCreDto implements BaseDto {
    @Validator(type = {MIN, NOT_NULL}, intValue = 1, name = "Id")
    private Integer id;

    @JsonProperty("Code")
    @Validator(type = {NOT_BLANK, NOT_NULL, PATTERN}, strValue = "[0-9]{3}", name = "Code")
    private String code;

    @JsonProperty("Ccy")
    @Validator(type = {NOT_BLANK, NOT_NULL, PATTERN}, strValue = "[A-Z]{3}", name = "Currency")
    private String currency;

    @JsonProperty("CcyNm_EN")
    @Validator(type = {NOT_BLANK, NOT_NULL}, name = "English name of currency")
    private String nameEnglish;

    @JsonProperty("CcyNm_UZ")
    @Validator(type = {NOT_BLANK, NOT_NULL}, name = "Uzbek name of currency")
    private String nameUzbek;

    @JsonProperty("CcyNm_UZC")
    @Validator(type = {NOT_BLANK, NOT_NULL}, name = "Uzbek-Krill name of currency")
    private String nameUzbekKrill;

    @JsonProperty("CcyNm_RU")
    @Validator(type = {NOT_BLANK, NOT_NULL}, name = "Russian name of currency")
    private String nameRussian;

    @JsonProperty("Nominal")
    @Validator(type = {NOT_BLANK, NOT_NULL, PATTERN}, strValue = "[1|10]", name = "Nominal")
    private String nominal;

    @JsonProperty("Rate")
    @Validator(type = {NOT_NULL}, name = "Rate")
    private BigDecimal rate;

    @JsonProperty("Diff")
    @Validator(type = {NOT_NULL}, name = "Difference")
    private BigDecimal difference;

    @JsonProperty("Date")
    @Validator(type = {NOT_BLANK, NOT_NULL, PATTERN}, strValue = "^([1-9]|0[1-9]|[12][0-9]|3[0-1])\\.([1-9]|0[1-9]|1[0-2])\\.\\d{4}$", name = "Date")
    private String date;
}
