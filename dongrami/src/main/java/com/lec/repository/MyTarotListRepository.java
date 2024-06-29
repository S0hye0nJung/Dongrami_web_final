package com.lec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lec.entity.SavedResult;

@Repository
public interface MyTarotListRepository extends JpaRepository<SavedResult, Integer> {

    @Query("SELECT sr FROM saved_result sr " +
            "JOIN FETCH sr.webReading wr " +
            "JOIN FETCH wr.subcategory sc " +
            "JOIN FETCH sc.maincategory mc")
    List<SavedResult> findAllWithDetails();
}
