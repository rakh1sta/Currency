package com.example.version_1.service.remoteServices.impls;

import com.example.version_1.dto.nbu.NbuCcyCreDto;
import com.example.version_1.entity.impls.NbuCcyEntity;
import com.example.version_1.enums.RemoteApiType;
import com.example.version_1.exception.customException.AlreadyCreatedException;
import com.example.version_1.exception.customException.NotFoundException;
import com.example.version_1.mapper.impls.NbuCcyMapper;
import com.example.version_1.service.RemoteService;
import com.example.version_1.service.remoteServices.AbstractService;
import com.example.version_1.service.remoteServices.ConcreteSubService;
import com.example.version_1.repositiry.impls.NbuCcyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class NbuCcyService extends AbstractService<NbuCcyRepository, NbuCcyMapper>
        implements RemoteService, ConcreteSubService {

    private static final Logger LOGGER = LogManager.getLogger(CbuCcyService.class);
    public static final RemoteApiType remoteType = RemoteApiType.NBU;

    protected NbuCcyService(RestTemplate restTemplate, ObjectMapper objectMapper, NbuCcyRepository repository, NbuCcyMapper mapper) {
        super(restTemplate, objectMapper, repository, mapper);
    }

    @Override
    public void saveListRemoteData() {
        super.saveAllRemoteData();
        LOGGER.info("All nbu currency successfully got from remote api ");
    }

    @Override
    public void save(String json) {
        try {
            NbuCcyCreDto dto = objectMapper.readValue(json, NbuCcyCreDto.class);
            Optional<NbuCcyEntity> byId = repository.findByCurrency(dto.getCurrency());
            if (byId.isPresent()) {
                LOGGER.info("Json Post already exist exception throwed. Dto is {}", dto);
                throw new AlreadyCreatedException("Post already exist");
            }
            LOGGER.info("Currency successfully created : {}", dto);
            repository.save(mapper.toEntity(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getRemoteApi() {
        return remoteType.getUrl();
    }

    @Override
    public BigDecimal calculateAverage(String currency) {
        return repository.findByCurrency(currency)
                .orElseThrow(() -> new NotFoundException("Not found rate by Currency"))
                .getRate();
    }
}
