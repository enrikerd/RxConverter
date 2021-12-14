package com.erd.rxconverter.domain;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "exchangerate" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "origincurrency")
	private String originCurrency;
	@Column(name = "origincurrencydescription")
	private String originCurrencyDescription;
	@Column(name = "destinationcurrency")
	private String destinationCurrency;
	@Column(name = "destinationcurrencydescription")
	private String destinationCurrencyDescription;
	@Column(name = "conversionfactor")
	private double conversionFactor;
	@Column(name = "keycf")
	private String keycf;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "updatedby")
	private  String updatedBy;
	@Column(name = "createdon")
	private ZonedDateTime createdOn;
	@Column(name = "updatedon")
	private ZonedDateTime updatedOn;
	@Column(name = "active")
	private boolean active;
}
