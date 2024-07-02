package com.lec.service;

import java.util.List;

import com.lec.dto.AdminReviewDto;

public interface AdminReviewService {

    List<AdminReviewDto> getAllReviews();

    long getReviewCount();

    void deleteReview(Integer reviewId);
}
