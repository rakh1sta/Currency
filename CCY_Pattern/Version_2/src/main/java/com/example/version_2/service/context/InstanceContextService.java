package com.example.version_2.service.context;

import com.example.version_2.service.RemoteService;
import com.example.version_2.service.composite.CompositeRemoteService;
import com.example.version_2.service.remoteServices.impls.CbuCcyService;
import com.example.version_2.service.remoteServices.impls.NbuCcyService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class InstanceContextService {

    @Autowired
    @Lazy
    private CompositeRemoteService defaultRemoteService;

    @Autowired
    private CbuCcyService cbuCcyService;

    @Autowired
    private NbuCcyService nbuCcyService;

    private final List<RemoteService> remoteServices = new ArrayList<>();
    @PostConstruct
    public void init() {
        remoteServices.add(cbuCcyService);
        remoteServices.add(nbuCcyService);
    }

    public RemoteService getService(String serviceName) {
        if (serviceName == null || Objects.equals(serviceName, "*") || Objects.equals(serviceName, "")) {
            return defaultRemoteService;
        }
        return findRemoteService(serviceName);
    }

    public List<RemoteService> getServices() {
        return remoteServices;
    }

    private RemoteService findRemoteService(String serviceName) {
        return remoteServices.stream()
                .filter(remoteService -> remoteService.getRemoteType().getNames().contains(serviceName))
                .findFirst()
                .orElse(defaultRemoteService);
    }

}
