package com.example.version_1.controller;

import com.example.version_1.service.remoteServices.MainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/remoteApi")
public class RemoteController {
    private static final Logger LOGGER = LogManager.getLogger(RemoteController.class);

    private final MainService service;

    public RemoteController(MainService service) {
        this.service = service;
    }


    @GetMapping("/load/{name}")
    public ResponseEntity<Boolean> loadRemoteData(@PathVariable String name) {
        LOGGER.info("Get request to load remote data from {} ", name);
        service.loadDataRemoteApi(name);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/average/{currency}")
    public ResponseEntity<BigDecimal> getAverageCurrencies(@PathVariable String currency) {
        LOGGER.info("Get request to load average currency from apis ");
        BigDecimal map = service.loadAverageCurrency("*", currency);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/{remoteApiName}/{currency}")
    public ResponseEntity<BigDecimal> getCurrency(@PathVariable String currency, @PathVariable String remoteApiName ) {
        LOGGER.info("Get request to load average currency from apis ");
        BigDecimal map = service.loadAverageCurrency(remoteApiName,currency);
        return ResponseEntity.ok(map);
    }


}
