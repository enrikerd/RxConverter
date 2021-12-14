package com.erd.rxconverter.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.erd.rxconverter.domain.ExchangeRate;
import com.erd.rxconverter.domain.ExchangeRateLog;
import com.erd.rxconverter.repository.ExchangeRateLogRepository;
import com.erd.rxconverter.service.dto.ExchangeRateLogServiceDto;
import com.erd.rxconverter.service.dto.ExchangeRateServiceDto;

import io.reactivex.Single;

@Service
public class ExchangeRateLogServiceImpl implements ExchangeRateLogService {

    @Autowired
    private ExchangeRateLogRepository exchangeRateLogRepository;

    @Override
    public Single<String> addExchangeRateLog(ExchangeRateLogServiceDto addExchangeRateLogServiceRequest) {
        return addExchangeRateLogToRepository(addExchangeRateLogServiceRequest);
    }

    private Single<String> addExchangeRateLogToRepository(ExchangeRateLogServiceDto addExchangeRateLogServiceRequest) {
        return Single.create(singleSubscriber -> {
            Long addedExchangeRateId = exchangeRateLogRepository.save(toExchangeRateLog(addExchangeRateLogServiceRequest)).getId();
            singleSubscriber.onSuccess(addedExchangeRateId.toString());
        });
    }

    private ExchangeRateLog toExchangeRateLog(ExchangeRateLogServiceDto addExchangeRateLogServiceRequest) {
    	ExchangeRateLog exchangeRateLog = new ExchangeRateLog();
        BeanUtils.copyProperties(addExchangeRateLogServiceRequest, exchangeRateLog);
        return exchangeRateLog;
    }
    
    
	@Override
	public Single<List<ExchangeRateLogServiceDto>> getAllExchangeRateLogs(int limit, int page) {
        return findAllExchangeRateLogsInRepository(limit, page)
                .map(this::toExchangeRateLogResponseList);
    }

    private Single<List<ExchangeRateLog>> findAllExchangeRateLogsInRepository(int limit, int page) {
        return Single.create(singleSubscriber -> {
            List<ExchangeRateLog> exchangeRateLogs = exchangeRateLogRepository.findAll(PageRequest.of(page, limit)).getContent();
            singleSubscriber.onSuccess(exchangeRateLogs);
        });
    }

    private List<ExchangeRateLogServiceDto> toExchangeRateLogResponseList(List<ExchangeRateLog> exchangeRateLogList) {
        return exchangeRateLogList
                .stream()
                .map(this::toExchangeRateLogResponse)
                .collect(Collectors.toList());
    }

    private ExchangeRateLogServiceDto toExchangeRateLogResponse(ExchangeRateLog exchangeRateLog) {
    	ExchangeRateLogServiceDto exchangeRateLogServiceDto = new ExchangeRateLogServiceDto();
        BeanUtils.copyProperties(exchangeRateLog, exchangeRateLogServiceDto);
        return exchangeRateLogServiceDto;
    }
}
