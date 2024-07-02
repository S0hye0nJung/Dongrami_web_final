package com.lec.service;

import com.lec.dto.AllReviewDTO;
import com.lec.entity.Review;

import java.util.List;
import java.util.Optional;

public interface AllReviewService {
    Review saveReview(Review review);

    List<AllReviewDTO> getAllReviewsWithNicknames();  // 반환 타입을 List<AllReviewDTO>로 변경

    Optional<Review> getReviewById(int id);

    void deleteReview(int id);
}
