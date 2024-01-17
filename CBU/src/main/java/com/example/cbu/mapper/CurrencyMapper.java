package com.example.cbu.mapper;

import com.example.cbu.dto.CurrencyCreDto;
import com.example.cbu.dto.CurrencyDto;
import com.example.cbu.entity.Currency;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends AbstractMapper<
        Currency,
        CurrencyDto,
        CurrencyCreDto> {

}
