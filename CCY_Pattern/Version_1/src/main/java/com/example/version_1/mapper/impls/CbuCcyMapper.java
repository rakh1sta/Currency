package com.example.version_1.mapper.impls;

import com.example.version_1.dto.cbu.CbuCcyCreDto;
import com.example.version_1.dto.cbu.CbuCcyDto;
import com.example.version_1.entity.impls.CbuCcyEntity;
import com.example.version_1.mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface CbuCcyMapper extends AbstractMapper<
        CbuCcyEntity,
        CbuCcyDto,
        CbuCcyCreDto> {

}
