package com.example.version_1.service;

import java.math.BigDecimal;

public interface RemoteService {
    void saveListRemoteData();
    BigDecimal calculateAverage(String currency);
}
