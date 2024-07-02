package com.lec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lec.dto.AdminReviewDto;
import com.lec.entity.Review;

@Repository
public interface AdminReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT new com.lec.dto.AdminReviewDto(r.reviewId, r.rating, r.reviewText, r.reviewCreate, r.reviewModify, m.createDate, m.userId, m.nickname) " +
           "FROM review r " +  // Review 엔티티를 review로 변경
           "JOIN r.member m")
    List<AdminReviewDto> findAllReviews();
}
