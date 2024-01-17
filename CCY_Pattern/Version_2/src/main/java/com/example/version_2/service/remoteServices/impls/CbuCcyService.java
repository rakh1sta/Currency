package com.example.version_2.service.remoteServices.impls;

import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.dto.cbu.CbuCcyRecuestDto;
import com.example.version_2.entity.impls.CbuCcyEntity;
import com.example.version_2.enums.RateType;
import com.example.version_2.exception.customException.AlreadyCreatedException;
import com.example.version_2.mapper.impls.CbuCcyMapper;
import com.example.version_2.repositiry.impls.CbuCcyRepository;
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

@Slf4j
@Service
public class CbuCcyService extends AbstractService<CbuCcyRepository, CbuCcyMapper> implements RemoteService {

    protected CbuCcyService(RestTemplate restTemplate, ObjectMapper objectMapper, CbuCcyRepository repository, CbuCcyMapper mapper) {
        super(restTemplate, objectMapper, repository, mapper);
    }

    @Override
    public CcyResponseDto save(String json) {
        try {
            CbuCcyRecuestDto dto = objectMapper.readValue(json, CbuCcyRecuestDto.class);
            Optional<CbuCcyEntity> byId = repository.findById(dto.getId());
            if (byId.isPresent()) {
                log.info("Already exist cbu currency {}", dto);
                throw new AlreadyCreatedException("Cbu currency already exist");
            }
            log.info("Cbu currency successfully created : {}", dto);
            CbuCcyEntity save = repository.save(mapper.toEntity(dto));
            return mapper.toDto(save);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CcyResponseDto> saveListRemoteData() {
        List<CcyResponseDto> ccyResponseDtos = super.saveAllRemoteData();
        log.info("All currencies got from cbu ");
        return ccyResponseDtos;
    }

    @Override
    public CcyResponseDto calculateAverage(String currency) {
        CbuCcyEntity cbuCcy = repository.findByCurrency(currency)
                .orElse(CbuCcyEntity.builder().rate(BigDecimal.ZERO).build());
        log.info("Calculated average rate ({}) of cbu",cbuCcy.getRate());
        return mapper.toDto(cbuCcy);
    }

    @Override
    public String getRemoteTypeUrl() {
        return getRemoteType().getUrl();
    }

    @Override
    public RateType getRemoteType() {
        return RateType.CBU;
    }
}
