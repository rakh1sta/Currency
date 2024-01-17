package com.example.version_2.controller;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.service.remoteServices.MainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/rate-api")
public class RemoteController {

    private final MainService service;

    @GetMapping("/load-rates/{remoteServiceName}")
    public ResponseEntity<List<CcyResponseDto>> loadCcyRates(@PathVariable String remoteServiceName) {
        log.info("Request to load ccy rates from {} ", remoteServiceName);
        List<CcyResponseDto> ccyResponseDtos = service.loadCurrencyRemoteService(remoteServiceName);
        return ResponseEntity.ok(ccyResponseDtos);
    }

    @GetMapping("/average/{ccy}")
    public ResponseEntity<CcyResponseDto> getAverageCurrencies(@PathVariable String ccy) {
        log.info("Request to get average currency");
        return ResponseEntity.ok(service.getAverageCcy("*", ccy));
    }

    @GetMapping("/{remoteServiceName}/{ccy}")
    public ResponseEntity<CcyResponseDto> getCurrency(@PathVariable String ccy, @PathVariable String remoteServiceName) {
        log.info("Request to get average currency {} from : {} ", ccy, remoteServiceName);
        return ResponseEntity.ok(service.getAverageCcy(remoteServiceName, ccy));
    }


}
