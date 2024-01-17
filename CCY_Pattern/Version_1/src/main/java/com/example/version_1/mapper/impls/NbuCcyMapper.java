package com.example.version_1.mapper.impls;

import com.example.version_1.dto.nbu.NbuCcyCreDto;
import com.example.version_1.dto.nbu.NbuCcyDto;
import com.example.version_1.entity.impls.NbuCcyEntity;
import com.example.version_1.mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NbuCcyMapper extends AbstractMapper<
        NbuCcyEntity,
        NbuCcyDto,
        NbuCcyCreDto> {
}
