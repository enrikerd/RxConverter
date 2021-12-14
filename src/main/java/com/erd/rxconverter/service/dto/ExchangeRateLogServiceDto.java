package com.erd.rxconverter.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateLogServiceDto {
	private Long id;
	private String originCurrency;
	private String originCurrencyDescription;
	private String destinationCurrency;
	private String destinationCurrencyDescription;
	private double conversionFactor;
	private double amount;
	private String operationMessage;
	private String createdBy;
	private ZonedDateTime createdOn;
	private boolean success;
}
