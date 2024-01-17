package com.example.cbu_caching.controller;

import com.example.cbu_caching.service.ValidatorContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import com.example.cbu_caching.dto.CurrencyCreDto;
import com.example.cbu_caching.dto.CurrencyDto;
import com.example.cbu_caching.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@AllArgsConstructor
@RestController
@RequestMapping("/currency")
public class CurrencyController {
    private static final Logger LOGGER = LogManager.getLogger(CurrencyController.class);

    private final CurrencyService service;
    private final ValidatorContext validatorContext;


    @GetMapping("/{ccyCode}")
    public ResponseEntity<CurrencyDto> getAllCurrencyByCode(@Pattern(regexp = "[0-9]{3}", message = "wrong format of cbu code")
                                                                  @Valid @PathVariable String ccyCode) {
        LOGGER.info("Get request to get currency by code ({}) ",ccyCode);
        CurrencyDto byCode = service.findByCode(ccyCode);
        return ResponseEntity.ok(byCode);
    }

    @PostMapping("/")
    public ResponseEntity<CurrencyDto> saveCbu(@Valid @RequestBody CurrencyCreDto dto) throws URISyntaxException {
        LOGGER.info("Post request to create  : {} ",dto);
        return ResponseEntity.created(new URI("/api/valyutas/")).body(service.save(dto));
    }




}
