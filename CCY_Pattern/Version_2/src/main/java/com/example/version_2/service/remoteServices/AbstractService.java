package com.example.version_2.service.remoteServices;

import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.exception.customException.InvalidValidationException;
import com.example.version_2.mapper.BaseMapper;
import com.example.version_2.repositiry.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.naming.NotContextException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Slf4j
public abstract class AbstractService< R extends BaseRepository, M extends BaseMapper> {
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

    public String getListDataFromRemote(String url) {
        try {
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            if (Objects.isNull(forEntity.getBody())) {
                log.error("Remote Service response is empty");
                throw new NotContextException(forEntity.getStatusCode().toString());
            }
            return forEntity.getBody();
        } catch (HttpServerErrorException.GatewayTimeout e) {
            log.error("Read Timeout exception when getting response of remote api");
            return "Timeout " + e.getStatusCode();
        } catch (NotContextException e) {
            log.error("No content exception when getting response of remote api");
            return e.getMessage();
        }
    }

    protected abstract CcyResponseDto save(String json);

    protected List<CcyResponseDto> saveAll(List<String> dtos) {
        List<CcyResponseDto> result = new ArrayList<>();
        if (dtos.isEmpty()) {
            log.warn("Currency list is empty");
            throw new InvalidValidationException("Empty Currency List");
        }
        dtos.forEach(dto -> result.add(save(dto.concat("}"))));
        return result;
    }

    protected List<CcyResponseDto> saveAllRemoteData() {
        String listRemoteData = getListDataFromRemote(getRemoteTypeUrl());
        String substring = listRemoteData.substring(1, listRemoteData.length() - 1);
        String[] split = substring.split("},");
        System.out.println(Arrays.toString(split));
        return saveAll(Arrays.asList(split));
    }

    protected abstract String getRemoteTypeUrl();

}
