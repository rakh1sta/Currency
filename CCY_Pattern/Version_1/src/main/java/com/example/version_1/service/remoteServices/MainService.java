package com.example.version_1.service.remoteServices;

import com.example.version_1.service.context.InstanceContextService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MainService {
    private final InstanceContextService context;
    public void loadDataRemoteApi(String remoteName) {
        context.getService(remoteName).saveListRemoteData();
    }
    public BigDecimal loadAverageCurrency(String remoteApiName,String currency) {
        return context.getService(remoteApiName).calculateAverage(currency);
    }
}
