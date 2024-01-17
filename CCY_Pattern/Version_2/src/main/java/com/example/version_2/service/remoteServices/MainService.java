package com.example.version_2.service.remoteServices;

import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.service.context.InstanceContextService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MainService {
    private final InstanceContextService context;
    public List<CcyResponseDto> loadCurrencyRemoteService(String remoteName) {
        return context.getService(remoteName).saveListRemoteData();
    }
    public CcyResponseDto getAverageCcy(String remoteApiName, String currency) {
        return context.getService(remoteApiName).calculateAverage(currency);
    }
}
