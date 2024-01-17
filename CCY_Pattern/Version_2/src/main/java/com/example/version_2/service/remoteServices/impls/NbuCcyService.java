package com.example.version_2.service.remoteServices.impls;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.dto.nbu.NbuCcyRecuestDto;
import com.example.version_2.entity.impls.NbuCcyEntity;
import com.example.version_2.enums.RateType;
import com.example.version_2.exception.customException.AlreadyCreatedException;
import com.example.version_2.mapper.impls.NbuCcyMapper;
import com.example.version_2.repositiry.impls.NbuCcyRepository;
import com.example.version_2.service.RemoteService;
import com.example.version_2.service.remoteServices.AbstractService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NbuCcyService extends AbstractService<NbuCcyRepository, NbuCcyMapper>
        implements RemoteService {

    protected NbuCcyService(RestTemplate restTemplate, ObjectMapper objectMapper, NbuCcyRepository repository, NbuCcyMapper mapper) {
        super(restTemplate, objectMapper, repository, mapper);
    }

    @Override
    public List<CcyResponseDto> saveListRemoteData() {
        List<CcyResponseDto> ccyResponseDtos = super.saveAllRemoteData();
        log.info("All currencies got from nbu ");
        return ccyResponseDtos;
    }

    @Override
    public CcyResponseDto save(String json) {
        try {
            NbuCcyRecuestDto dto = objectMapper.readValue(json, NbuCcyRecuestDto.class);
            Optional<NbuCcyEntity> byId = repository.findByCurrency(dto.getCurrency());
            if (byId.isPresent()) {
                log.info("Already exist cby currency {}", dto);
                throw new AlreadyCreatedException("Cbu currency already exist");
            }
            log.info("Nbu currency successfully created : {}", dto);
            NbuCcyEntity save = repository.save(mapper.toEntity(dto));
            return mapper.toDto(save);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public CcyResponseDto calculateAverage(String currency) {
        NbuCcyEntity nbuCcy = repository.findByCurrency(currency)
                .orElse(NbuCcyEntity.builder().rate(BigDecimal.ZERO).build());
        log.info("Calculated average rate ({}) of nbu",nbuCcy.getRate());
        return mapper.toDto(nbuCcy);
    }
    @Override
    public RateType getRemoteType() {
        return RateType.NBU;
    }

    @Override
    public String getRemoteTypeUrl() {
        return getRemoteType().getUrl();
    }


}
