package com.erd.rxconverter.service;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.erd.rxconverter.domain.ExchangeRate;
import com.erd.rxconverter.domain.ExchangeRateLog;
import com.erd.rxconverter.repository.ExchangeRateLogRepository;
import com.erd.rxconverter.repository.ExchangeRateRepository;
import com.erd.rxconverter.service.dto.ExchangeRateOperationServiceDto;
import com.erd.rxconverter.service.dto.ExchangeRateServiceDto;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    
    @Autowired
    private ExchangeRateLogRepository exchangeRateLogRepository;

    @Override
    public Single<String> addExchangeRate(ExchangeRateServiceDto exchangeRateServiceDto) {
        return addExchangeRateToRepository(exchangeRateServiceDto);
    }

    private Single<String> addExchangeRateToRepository(ExchangeRateServiceDto exchangeRateServiceDto) {
        return Single.create(singleSubscriber -> {
            Long addedExchangeRateId = exchangeRateRepository.save(toExchangeRate(exchangeRateServiceDto)).getId();
            singleSubscriber.onSuccess(addedExchangeRateId.toString());
        });
    }

    private ExchangeRate toExchangeRate(ExchangeRateServiceDto exchangeRateServiceDto) {
    	ExchangeRate exchangeRate = new ExchangeRate();
        BeanUtils.copyProperties(exchangeRateServiceDto, exchangeRate);
        exchangeRate.setKeycf(exchangeRateServiceDto.getDestinationCurrency().concat(exchangeRateServiceDto.getDestinationCurrency()));
        ZonedDateTime date = ZonedDateTime.now();
        exchangeRate.setCreatedOn(date);
        exchangeRate.setUpdatedOn(date);
        exchangeRate.setActive(true);
        return exchangeRate;
    }

	@Override
	public Completable updateExchangeRate(ExchangeRateServiceDto exchangeRateServiceDto) {
		return updateExchangeRateToRepository(exchangeRateServiceDto);
	}
	
    private Completable updateExchangeRateToRepository(ExchangeRateServiceDto exchangeRateServiceDto) {
        return Completable.create(completableSubscriber -> {
            Optional<ExchangeRate> optionalExchangeRate = exchangeRateRepository.findById(exchangeRateServiceDto.getId());
            if (!optionalExchangeRate.isPresent())
                completableSubscriber.onError(new EntityNotFoundException());
            else {
            	ExchangeRate exchangeRate = optionalExchangeRate.get();
            	//exchangeRate.setActive(exchangeRateServiceDto.isActive());
            	exchangeRate.setConversionFactor(exchangeRateServiceDto.getConversionFactor());
            	exchangeRate.setDestinationCurrency(exchangeRateServiceDto.getDestinationCurrency());
            	exchangeRate.setDestinationCurrencyDescription(exchangeRateServiceDto.getDestinationCurrencyDescription());
            	exchangeRate.setOriginCurrency(exchangeRateServiceDto.getOriginCurrency());
            	exchangeRate.setOriginCurrencyDescription(exchangeRateServiceDto.getOriginCurrencyDescription());
            	//exchangeRate.setKeycf(exchangeRateServiceDto.getKeycf());
            	exchangeRate.setUpdatedBy(exchangeRateServiceDto.getUpdatedBy());
            	exchangeRate.setUpdatedOn(ZonedDateTime.now());
            	exchangeRateRepository.save(exchangeRate);
                completableSubscriber.onComplete();
            }
        });
    }

	@Override
	public Single<List<ExchangeRateServiceDto>> getAllExchangeRates(int limit, int page) {
        return findAllExchangeRatesInRepository(limit, page)
                .map(this::toExchangeRateResponseList);
    }

    private Single<List<ExchangeRate>> findAllExchangeRatesInRepository(int limit, int page) {
        return Single.create(singleSubscriber -> {
            List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll(PageRequest.of(page, limit)).getContent();
            singleSubscriber.onSuccess(exchangeRates);
        });
    }

    private List<ExchangeRateServiceDto> toExchangeRateResponseList(List<ExchangeRate> exchangeRateList) {
        return exchangeRateList
                .stream()
                .map(this::toExchangeRateResponse)
                .collect(Collectors.toList());
    }

    private ExchangeRateServiceDto toExchangeRateResponse(ExchangeRate exchangeRate) {
    	ExchangeRateServiceDto exchangeRateServiceDto = new ExchangeRateServiceDto();
        BeanUtils.copyProperties(exchangeRate, exchangeRateServiceDto);
        return exchangeRateServiceDto;
    }

	@Override
	public Single<ExchangeRateServiceDto> getExchangeRateDetail(String keycf) {
        return findExchangeRateDetailInRepository(keycf);
    }

    private Single<ExchangeRateServiceDto> findExchangeRateDetailInRepository(String keycf) {
        return Single.create(singleSubscriber -> {
            Optional<ExchangeRate> optionalExchangeRate = exchangeRateRepository.findBykeycf(keycf);
            if (!optionalExchangeRate.isPresent())
                singleSubscriber.onError(new EntityNotFoundException());
            else {
            	ExchangeRateServiceDto exchangeRateServiceDto = toExchangeRateResponse(optionalExchangeRate.get());
                singleSubscriber.onSuccess(exchangeRateServiceDto);
            }
        });
    }

	@Override
	public Single<ExchangeRateOperationServiceDto> exchangeRateOperation(ExchangeRateOperationServiceDto exchangeRateOperationServiceDto) {
		// TODO Auto-generated method stub
		return calcExchangeRateOperation(exchangeRateOperationServiceDto);
	}
	
    private Single<ExchangeRateOperationServiceDto> calcExchangeRateOperation(ExchangeRateOperationServiceDto exchangeRateOperationServiceDto) {
        return Single.create(singleSubscriber -> {
            Optional<ExchangeRate> optionalExchangeRate = exchangeRateRepository.findBykeycf(exchangeRateOperationServiceDto.getOriginCurrency().concat(exchangeRateOperationServiceDto.getDestinationCurrency()));
            if (!optionalExchangeRate.isPresent())
            	singleSubscriber.onError(new EntityNotFoundException());
            else {
            	ExchangeRateServiceDto exchangeRateServiceDto = toExchangeRateResponse(optionalExchangeRate.get());
        		double monedadestino = exchangeRateOperationServiceDto.getAmount()*exchangeRateServiceDto.getConversionFactor();
        		String message =exchangeRateOperationServiceDto.getAmount() +" "+ exchangeRateServiceDto.getOriginCurrencyDescription()+" equivalen a " + monedadestino + " "+exchangeRateServiceDto.getDestinationCurrencyDescription();
        		log.debug(message);
        		
        		//monedadestino = exchangeRateOperationServiceDto.getAmmount()/exchangeRateServiceDto.getConversionFactor();
        		//System.out.println(exchangeRateOperationServiceDto.getAmmount() + " soles equivalen a " + monedadestino + " dolares");
        		
        		
            	ExchangeRateOperationServiceDto response = new ExchangeRateOperationServiceDto();
            	response.setOperationMessage(message);
            	response.setOriginDetail("1 "+exchangeRateServiceDto.getOriginCurrency()+" = "+exchangeRateServiceDto.getConversionFactor()+" "+exchangeRateServiceDto.getDestinationCurrency() );
            	response.setDestinationDetail("1 "+exchangeRateServiceDto.getDestinationCurrency()+" = "+1/exchangeRateServiceDto.getConversionFactor()+" "+exchangeRateServiceDto.getOriginCurrency() );
            	//response.se
            	ExchangeRateLog exchangeRateLog = new ExchangeRateLog();
            	exchangeRateLog.setAmount(exchangeRateOperationServiceDto.getAmount());
            	exchangeRateLog.setConversionFactor(exchangeRateServiceDto.getConversionFactor());
            	exchangeRateLog.setCreatedBy(exchangeRateOperationServiceDto.getCreatedBy());
            	exchangeRateLog.setCreatedOn(ZonedDateTime.now());
            	exchangeRateLog.setDestinationCurrency(exchangeRateServiceDto.getDestinationCurrency());
            	exchangeRateLog.setDestinationCurrencyDescription(exchangeRateServiceDto.getDestinationCurrencyDescription());
            	exchangeRateLog.setOperationMessage(message);
            	exchangeRateLog.setOriginCurrency(exchangeRateServiceDto.getOriginCurrency());
            	exchangeRateLog.setOriginCurrencyDescription(exchangeRateServiceDto.getOriginCurrencyDescription());
            	exchangeRateLog.setSuccess(true);
            	exchangeRateLogRepository.save(exchangeRateLog);
            	singleSubscriber.onSuccess(response);
            }
        });
    }
    
    
}
