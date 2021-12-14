package com.erd.rxconverter.controller.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateOperationWebRequest {

	private String originCurrency;
	private String destinationCurrency;
	private double amount;
	private String createdBy;

}
