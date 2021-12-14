package com.erd.rxconverter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erd.rxconverter.domain.ExchangeRate;
import com.erd.rxconverter.domain.ExchangeRateLog;

public interface ExchangeRateLogRepository extends JpaRepository<ExchangeRateLog, Long> {
	
}
