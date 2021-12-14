package com.erd.rxconverter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erd.rxconverter.domain.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
	
    @Query(value = "SELECT * FROM ExchangeRate u WHERE u.keycf = ?1", nativeQuery = true)
    Optional<ExchangeRate> findBykeycf(String keycf);

}
