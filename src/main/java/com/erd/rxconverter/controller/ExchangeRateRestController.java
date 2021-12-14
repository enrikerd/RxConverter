package com.erd.rxconverter.controller;

import java.net.URI;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erd.rxconverter.controller.dto.request.AddExchangeRateWebRequest;
import com.erd.rxconverter.controller.dto.request.ExchangeRateOperationWebRequest;
import com.erd.rxconverter.controller.dto.response.BaseWebResponse;
import com.erd.rxconverter.controller.dto.response.ExchangeRateLogWebResponse;
import com.erd.rxconverter.controller.dto.response.ExchangeRateOperationWebResponse;
import com.erd.rxconverter.controller.dto.response.ExchangeRateWebResponse;
import com.erd.rxconverter.service.ExchangeRateLogService;
import com.erd.rxconverter.service.ExchangeRateService;
import com.erd.rxconverter.service.dto.ExchangeRateLogServiceDto;
import com.erd.rxconverter.service.dto.ExchangeRateOperationServiceDto;
import com.erd.rxconverter.service.dto.ExchangeRateServiceDto;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/exchangerate")
public class ExchangeRateRestController {

    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private ExchangeRateLogService exchangeRateLogService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    ) public Single<ResponseEntity<BaseWebResponse>> addExchangeRate(
        @RequestBody AddExchangeRateWebRequest addExchangeRateWebRequest) {
        return exchangeRateService.addExchangeRate(toAddExchangeRateRequest(addExchangeRateWebRequest)).subscribeOn(Schedulers.io()).map(
            s -> ResponseEntity.created(URI.create("/api/v1/exchangerate/" + s))
                .body(BaseWebResponse.successNoData()));
    }

    private ExchangeRateServiceDto toAddExchangeRateRequest(AddExchangeRateWebRequest addExchangeRateWebRequest) {
    	ExchangeRateServiceDto addExchangeRateServiceRequest = new ExchangeRateServiceDto();
        BeanUtils.copyProperties(addExchangeRateWebRequest, addExchangeRateServiceRequest);
        return addExchangeRateServiceRequest;
    }


    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<BaseWebResponse>> updateExchangeRate(@PathVariable(value = "id") Long id,
                                                              @RequestBody AddExchangeRateWebRequest addExchangeRateWebRequest) {
        return exchangeRateService.updateExchangeRate(toUpdateExchangeRateRequest(id, addExchangeRateWebRequest))
                .subscribeOn(Schedulers.io())
                .toSingle(() -> ResponseEntity.ok(BaseWebResponse.successNoData()));
    }

    private ExchangeRateServiceDto toUpdateExchangeRateRequest(Long id, AddExchangeRateWebRequest AddExchangeRateWebRequest) {
    	ExchangeRateServiceDto exchangeRateServiceDto = new ExchangeRateServiceDto();
        BeanUtils.copyProperties(AddExchangeRateWebRequest, exchangeRateServiceDto);
        exchangeRateServiceDto.setId(id);
        return exchangeRateServiceDto;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<List<ExchangeRateWebResponse>>> getAllExchangeRates(@RequestParam(value = "limit", defaultValue = "5") int limit,
                                                                                      @RequestParam(value = "page", defaultValue = "0") int page) {
        return exchangeRateService.getAllExchangeRates(limit, page)
                .subscribeOn(Schedulers.io())
                .map(exchangeRateLogServiceDtos -> ResponseEntity.ok(toExchangeRateWebResponseWebResponseList(exchangeRateLogServiceDtos)));
    }

    private List<ExchangeRateWebResponse> toExchangeRateWebResponseWebResponseList(List<ExchangeRateServiceDto> exchangeRateServiceDtoDtoList) {
        return exchangeRateServiceDtoDtoList
                .stream()
                .map(this::toExchangeRateWebResponse)
                .collect(Collectors.toList());
    }

    private ExchangeRateWebResponse toExchangeRateWebResponse(ExchangeRateServiceDto exchangeRateServiceDto) {
    	ExchangeRateWebResponse exchangeRateWebResponse = new ExchangeRateWebResponse();
        BeanUtils.copyProperties(exchangeRateServiceDto, exchangeRateWebResponse);
        return exchangeRateWebResponse;
    }


    private ExchangeRateOperationServiceDto toAddExchangeRateRequest(ExchangeRateOperationWebRequest exchangeRateOperationWebRequest) {
    	ExchangeRateOperationServiceDto exchangeRateOperationServiceDto = new ExchangeRateOperationServiceDto();
        BeanUtils.copyProperties(exchangeRateOperationWebRequest, exchangeRateOperationServiceDto);
        return exchangeRateOperationServiceDto;
    }
    
    @PostMapping(
    		value = "/operation",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity> exchangeRateOperation(@RequestBody ExchangeRateOperationWebRequest exchangeRateOperationWebRequest) {
        return exchangeRateService.exchangeRateOperation(toAddExchangeRateRequest(exchangeRateOperationWebRequest))
                .subscribeOn(Schedulers.io())
                .map(response -> ResponseEntity.ok((toExchangeRateOperationWebResponse(response))));
        
    }


    private ExchangeRateOperationWebResponse toExchangeRateOperationWebResponse(ExchangeRateOperationServiceDto exchangeRateOperationServiceDto) {
    	ExchangeRateOperationWebResponse exchangeRateOperationWebResponse = new ExchangeRateOperationWebResponse();
    	BeanUtils.copyProperties(exchangeRateOperationServiceDto, exchangeRateOperationWebResponse);
        return exchangeRateOperationWebResponse;
    }
    
    /*Operation Log*/
    @GetMapping(
    		value = "/log",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<List<ExchangeRateLogWebResponse>>> getAllExchangeRateLogs(@RequestParam(value = "limit", defaultValue = "5") int limit,
                                                                                      @RequestParam(value = "page", defaultValue = "0") int page) {
        return exchangeRateLogService.getAllExchangeRateLogs(limit, page)
                .subscribeOn(Schedulers.io())
                .map(exchangeRateLogServiceDtos -> ResponseEntity.ok(toExchangeRateLogWebResponseWebResponseList(exchangeRateLogServiceDtos)));
    }

    private List<ExchangeRateLogWebResponse> toExchangeRateLogWebResponseWebResponseList(List<ExchangeRateLogServiceDto> exchangeRateLogServiceDtoDtoList) {
        return exchangeRateLogServiceDtoDtoList
                .stream()
                .map(this::toExchangeRateLogWebResponse)
                .collect(Collectors.toList());
    }

    private ExchangeRateLogWebResponse toExchangeRateLogWebResponse(ExchangeRateLogServiceDto exchangeRateLogServiceDto) {
    	ExchangeRateLogWebResponse exchangeRateLogWebResponse = new ExchangeRateLogWebResponse();
        BeanUtils.copyProperties(exchangeRateLogServiceDto, exchangeRateLogWebResponse);
        return exchangeRateLogWebResponse;
    }

}
