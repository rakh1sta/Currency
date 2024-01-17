package com.example.version_2.mapper.impls;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.dto.nbu.NbuCcyRecuestDto;
import com.example.version_2.entity.impls.NbuCcyEntity;
import com.example.version_2.mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NbuCcyMapper extends AbstractMapper<
        NbuCcyEntity,
        CcyResponseDto,
        NbuCcyRecuestDto> {
}
