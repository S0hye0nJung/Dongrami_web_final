package com.lec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lec.entity.Review;

public interface MyReviewRepository extends JpaRepository<Review, Integer>{
	
	@Query("SELECT r FROM review r ORDER BY GREATEST(r.reviewCreate, COALESCE(r.reviewModify, r.reviewCreate)) DESC")
    List<Review> findAllOrderByLatestDateDesc();
}
