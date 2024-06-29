package com.lec.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer>{

}
