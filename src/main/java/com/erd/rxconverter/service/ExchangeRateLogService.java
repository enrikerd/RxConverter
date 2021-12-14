package com.erd.rxconverter.service;

import java.util.List;

import com.erd.rxconverter.service.dto.ExchangeRateLogServiceDto;
import com.erd.rxconverter.service.dto.ExchangeRateServiceDto;

import io.reactivex.Single;

public interface ExchangeRateLogService {
    Single<String> addExchangeRateLog(ExchangeRateLogServiceDto addExchangeRateLogServiceRequest);
    Single<List<ExchangeRateLogServiceDto>> getAllExchangeRateLogs(int limit, int page);
}
