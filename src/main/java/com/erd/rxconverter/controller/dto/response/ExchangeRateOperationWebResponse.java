package com.erd.rxconverter.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateOperationWebResponse {
	private String operationMessage;
	private String originDetail ;
	private String destinationDetail ;
}
