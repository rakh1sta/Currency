package com.example.version_1.service.remoteServices;


import com.example.version_1.exception.customException.InvalidValidationException;
import com.example.version_1.mapper.BaseMapper;
import com.example.version_1.repositiry.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.naming.NotContextException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public abstract class AbstractService< R extends BaseRepository, M extends BaseMapper> {
    private static final Logger LOGGER = LogManager.getLogger(AbstractService.class);

    private final RestTemplate restTemplate;
    protected final ObjectMapper objectMapper;
    protected final R repository;
    protected final M mapper;

    protected AbstractService(RestTemplate restTemplate, ObjectMapper objectMapper, R repository, M mapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.mapper = mapper;
    }

    protected String getListDataFromRemote() {
        try {
            ResponseEntity<String> forEntity = restTemplate.getForEntity(getRemoteApi(), String.class);
            if (Objects.isNull(forEntity.getBody())) {
                LOGGER.error("Remote api request's response is empty");
                throw new NotContextException("No Content");
            }
            return forEntity.getBody();
        } catch (HttpServerErrorException.GatewayTimeout e) {
            LOGGER.error("Read Timeout exception when getting response of remote api");
            return "Timeout " + e.getStatusCode();
        } catch (NotContextException e) {
            LOGGER.error("No content exception when getting response of remote api");
            return e.getMessage();
        }
    }

    protected abstract void save(String json);

    protected void saveAll(List<String> dtos) {
        if (dtos.isEmpty()) {
            LOGGER.warn("List is empty");
            throw new InvalidValidationException("Empty List");
        }
        dtos.forEach(dto -> save(dto.concat("}")));
    }

    protected void saveAllRemoteData() {
        String listRemoteData = getListDataFromRemote();
        String substring = listRemoteData.substring(1, listRemoteData.length() - 1);
        String[] split = substring.split("},");
        saveAll(Arrays.asList(split));
    }

    protected abstract String getRemoteApi();

}
