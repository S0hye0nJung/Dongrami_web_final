package com.lec.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.lec.entity.SavedResult;

@Repository
public interface SavedResultRepository extends JpaRepository<SavedResult, Integer> {

 }



