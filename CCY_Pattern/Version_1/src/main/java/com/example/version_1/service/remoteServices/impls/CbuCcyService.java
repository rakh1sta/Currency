package com.example.version_1.service.remoteServices.impls;

import com.example.version_1.dto.cbu.CbuCcyCreDto;
import com.example.version_1.entity.impls.CbuCcyEntity;
import com.example.version_1.enums.RemoteApiType;
import com.example.version_1.exception.customException.AlreadyCreatedException;
import com.example.version_1.exception.customException.NotFoundException;
import com.example.version_1.mapper.impls.CbuCcyMapper;
import com.example.version_1.repositiry.impls.CbuCcyRepository;
import com.example.version_1.service.RemoteService;
import com.example.version_1.service.remoteServices.AbstractService;
import com.example.version_1.service.remoteServices.ConcreteSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CbuCcyService extends AbstractService<CbuCcyRepository, CbuCcyMapper>
        implements RemoteService, ConcreteSubService {

    private static final Logger LOGGER = LogManager.getLogger(CbuCcyService.class);
    public static final RemoteApiType remoteType = RemoteApiType.CBU;


    protected CbuCcyService(RestTemplate restTemplate, ObjectMapper objectMapper, CbuCcyRepository repository, CbuCcyMapper mapper) {
        super(restTemplate, objectMapper, repository, mapper);
    }

    @Override
    public void save(String json) {
        try {
            CbuCcyCreDto dto = objectMapper.readValue(json, CbuCcyCreDto.class);
            Optional<CbuCcyEntity> byId = repository.findById(dto.getId());
            if (byId.isPresent()) {
                LOGGER.info("Json Post already exist exception throwed. Dto is {}", dto);
                throw new AlreadyCreatedException("Post already exist");
            }
            LOGGER.info("Currency successfully created : {}", dto);
            CbuCcyEntity entity = mapper.toEntity(dto);
            repository.save(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveListRemoteData() {
        super.saveAllRemoteData();
        LOGGER.info("All currencies successfully got from remote api ");
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
