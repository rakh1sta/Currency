package com.example.version_2.service.composite;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.enums.RateType;
import com.example.version_2.service.RemoteService;
import com.example.version_2.service.context.InstanceContextService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CompositeRemoteService implements RemoteService {
    private final Set<RemoteService> list = new HashSet<>();
    private final InstanceContextService context;

    public CompositeRemoteService(InstanceContextService context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        List<RemoteService> services = context.getServices();
        list.addAll(services);
        System.out.println("Init services");
    }

    @Override
    public CcyResponseDto calculateAverage(String currency) {
        CcyResponseDto build = CcyResponseDto.builder().currency(currency).build();
        BigDecimal average = BigDecimal.ZERO;
        for (RemoteService remoteService : list) {
            CcyResponseDto ccyResponseDto = remoteService.calculateAverage(currency);
            build.setDate(ccyResponseDto.getDate());
            build.setTitle(ccyResponseDto.getTitle());
            average = average.add(ccyResponseDto.getRate());
        }
        average = average.divide(BigDecimal.valueOf(list.size()));
        build.setRate(average);
        log.info("Calculated average rate ({}) of remote services",average);
        return build;
    }

    @Override
    public RateType getRemoteType() {
        return RateType.ALL;
    }

    @Override
    public List<CcyResponseDto> saveListRemoteData() {
        return List.of();
    }
}
