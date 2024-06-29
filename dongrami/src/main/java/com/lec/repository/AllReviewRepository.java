package com.lec.repository;

import com.lec.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllReviewRepository extends JpaRepository<Review, Integer> {
}
