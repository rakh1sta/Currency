package com.example.cbu.service;

import com.example.cbu.dto.CurrencyCreDto;
import com.example.cbu.dto.CurrencyDto;
import com.example.cbu.entity.Currency;
import com.example.cbu.exception.customException.AlreadyCreatedException;
import com.example.cbu.exception.customException.InvalidValidationException;
import com.example.cbu.exception.customException.NotFoundException;
import com.example.cbu.mapper.CurrencyMapper;
import com.example.cbu.repositiry.CurrencyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.naming.NotContextException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CurrencyService {

    private static final Logger LOGGER = LogManager.getLogger(CurrencyService.class);
    private final CurrencyRepository repository;
    private final CurrencyMapper mapper;

    private final RestTemplate restTemplate;

    public CurrencyService(CurrencyRepository repository, CurrencyMapper mapper, RestTemplate restTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    public CurrencyDto save(CurrencyCreDto dto) {
        Optional<Currency> byId = repository.findById(dto.getId());
        if (byId.isPresent()) {
            LOGGER.info("Currency already exist exception throwed. Dto is {}", dto);
            throw new AlreadyCreatedException("Currency already exist");
        }
        LOGGER.info("Currency successfully created : {}", dto);
        Currency currency = repository.save(mapper.toEntity(dto));
        return mapper.toDto(currency);
    }


    public void saveAll(List<CurrencyCreDto> dtos) {
        if (dtos.isEmpty()) {
            throw new InvalidValidationException("Empty List");
        }
        dtos.forEach(this::save);
    }


    public CurrencyDto findByCode(String ccyCode) {
        Optional<Currency> currency = repository.findByCode(ccyCode);
        if (currency.isEmpty()) {
            LOGGER.info("Currency not found by ccyCode ({})", ccyCode);
            throw new NotFoundException("Currency not found by code");
        }
        CurrencyDto dto = mapper.toDto(currency.get());
        LOGGER.info("Currency found by ccyCode ({}) dto -> {}", ccyCode, dto);
        return dto;
    }

    public String getListCurrencyRemote() {
        try {
            ResponseEntity<String> forEntity = restTemplate.getForEntity("https://cbu.uz/uz/arkhiv-kursov-valyut/json/", String.class);
            if (Objects.isNull(forEntity.getBody())) {
                throw new NotContextException("No Content");
            }
            return forEntity.getBody();
        } catch (HttpServerErrorException.GatewayTimeout e) {
            return "Timeout " + e.getStatusCode();
        } catch (NotContextException e) {
            return e.getMessage();
        }
    }


    //    @Bean
    public CommandLineRunner getAllCurrencies() {
        return args -> {
            ObjectMapper objectMapper = new ObjectMapper();
            List<CurrencyCreDto> dtos = objectMapper.readValue(getListCurrencyRemote(), new TypeReference<>() {
            });
            saveAll(dtos);
            LOGGER.info("All currencies successfully get ");
            System.out.println("Successfully currencies loaded to db");
        };
    }
}
