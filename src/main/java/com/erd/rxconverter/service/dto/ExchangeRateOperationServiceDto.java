package com.erd.rxconverter.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateOperationServiceDto {

	private String originCurrency;
	private String destinationCurrency;
	private double amount;
	private String operationMessage;
	private String originDetail ;
	private String destinationDetail ;
	private String createdBy;

}
