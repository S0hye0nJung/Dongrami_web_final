package com.lec.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lec.dto.AdminReviewDto;
import com.lec.entity.Review;
import com.lec.repository.AdminReviewRepository;
import com.lec.service.AdminReviewService;

@Service
public class AdminReviewServiceImpl implements AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;

    @Autowired
    public AdminReviewServiceImpl(AdminReviewRepository adminReviewRepository) {
        this.adminReviewRepository = adminReviewRepository;
    }

    @Override
    public List<AdminReviewDto> getAllReviews() {
        return adminReviewRepository.findAllReviews();
    }

    @Override
    public long getReviewCount() {
        return adminReviewRepository.count();
    }

    @Override
    public void deleteReview(Integer reviewId) {
        Optional<Review> optionalReview = adminReviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            adminReviewRepository.delete(optionalReview.get());
        } else {
            throw new RuntimeException("Review not found with id: " + reviewId);
        }
    }
}
