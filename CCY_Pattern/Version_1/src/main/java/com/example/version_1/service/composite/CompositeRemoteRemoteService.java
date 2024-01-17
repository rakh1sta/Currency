package com.example.version_1.service.composite;

import com.example.version_1.service.RemoteService;
import com.example.version_1.service.context.InstanceContextService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompositeRemoteRemoteService implements RemoteService {
    private final List<RemoteService> list = new ArrayList<>();
    private final InstanceContextService context;

    public CompositeRemoteRemoteService(InstanceContextService context) {
        this.context = context;
    }
//    @PostConstruct
    public void init() {
        List<RemoteService> services = context.getConcreteServices();
        list.addAll(services);
        System.out.println("Init services");
    }

    @Override
    public BigDecimal calculateAverage(String currency) {
        init();
        BigDecimal res = new BigDecimal(0);
        for (RemoteService remoteService : list) {
            System.out.println(remoteService.calculateAverage(currency));
            res = res.add(remoteService.calculateAverage(currency));
            System.out.println(res);
        }
        return res.divide(BigDecimal.valueOf(list.size()));
    }

    @Override
    public void saveListRemoteData() {

    }
}
