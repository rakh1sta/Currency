package com.example.cbu_caching.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.example.cbu_caching.dto.CurrencyCreDto;
import com.example.cbu_caching.dto.CurrencyDto;
import com.example.cbu_caching.entity.Currency;


@Component
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends AbstractMapper<
        Currency,
        CurrencyDto,
        CurrencyCreDto> {

}
