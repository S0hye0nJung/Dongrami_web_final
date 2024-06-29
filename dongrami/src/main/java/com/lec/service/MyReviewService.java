package com.lec.service;

import java.util.List;

import com.lec.dto.MyReviewDTO;

public interface MyReviewService {

    List<MyReviewDTO> getAllReviewDTOs();
    void deleteReview(int reviewId);
    
    MyReviewDTO updateReview(int reviewId, MyReviewDTO updatedReviewDTO);
}
