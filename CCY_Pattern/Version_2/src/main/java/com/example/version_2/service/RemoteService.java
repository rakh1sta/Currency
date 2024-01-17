package com.example.version_2.service;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.enums.RateType;

import java.util.List;

public interface RemoteService {
    List<CcyResponseDto> saveListRemoteData();

    CcyResponseDto calculateAverage(String currency);

    RateType getRemoteType();

}
