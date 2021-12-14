package com.erd.rxconverter.service;

import java.util.List;

import com.erd.rxconverter.service.dto.ExchangeRateOperationServiceDto;
import com.erd.rxconverter.service.dto.ExchangeRateServiceDto;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface ExchangeRateService {
    Single<String> addExchangeRate(ExchangeRateServiceDto addExchangeRateServiceRequest);
    Completable updateExchangeRate(ExchangeRateServiceDto updatechangeRateServiceRequest);

    Single<List<ExchangeRateServiceDto>> getAllExchangeRates(int limit, int page);

    Single<ExchangeRateServiceDto> getExchangeRateDetail(String keycf);
    
    Single<ExchangeRateOperationServiceDto> exchangeRateOperation(ExchangeRateOperationServiceDto exchangeRateOperationServiceDto);
}
