package com.erd.rxconverter.service.dto;

import java.time.LocalDate;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateServiceDto {
	private Long id;
	private String originCurrency;
	private String originCurrencyDescription;
	private String destinationCurrency;
	private String destinationCurrencyDescription;
	private double conversionFactor;
	private String keycf;
	private String createdBy;
	private  String updatedBy;
	private LocalDate createdOn;
	private LocalDate updatedOn;
	private boolean active;
}
