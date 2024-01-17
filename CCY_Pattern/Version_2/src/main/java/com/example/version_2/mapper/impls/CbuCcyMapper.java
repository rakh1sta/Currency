package com.example.version_2.mapper.impls;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.dto.cbu.CbuCcyRecuestDto;
import com.example.version_2.entity.impls.CbuCcyEntity;
import com.example.version_2.mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface CbuCcyMapper extends AbstractMapper<
        CbuCcyEntity,
        CcyResponseDto,
        CbuCcyRecuestDto> {

    @Override
    @Mapping(target = "title",source = "nameUzbek")
    CcyResponseDto toDto(CbuCcyEntity cbuCcyEntity);
}
