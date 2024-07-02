package com.lec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lec.entity.WebReading;

public interface WebReadingRepository extends JpaRepository<WebReading, Integer> {
}
