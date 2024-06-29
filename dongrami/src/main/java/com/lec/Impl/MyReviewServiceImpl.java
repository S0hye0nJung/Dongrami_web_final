package com.lec.Impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lec.dto.MyReviewDTO;
import com.lec.entity.Review;
import com.lec.repository.MyReviewRepository;
import com.lec.service.MyReviewService;

import jakarta.transaction.Transactional;

@Service
public class MyReviewServiceImpl implements MyReviewService {

    private final MyReviewRepository myReviewRepository;

    @Autowired
    public MyReviewServiceImpl(MyReviewRepository myReviewRepository) {
        this.myReviewRepository = myReviewRepository;
    }

    @Override
    public List<MyReviewDTO> getAllReviewDTOs() {
        List<Review> reviews = myReviewRepository.findAllOrderByLatestDateDesc();
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MyReviewDTO convertToDTO(Review review) {
        return MyReviewDTO.builder()
                .reviewId(review.getReviewId())
                .mainCategoryName(review.getSubcategory().getMaincategory().getMaincategory_name()) // 이 부분은 실제 데이터 모델에 맞게 수정 필요
                .reviewText(review.getReviewText())
                .reviewCreate(review.getReviewCreate())
                .build();
    }

    @Override
    public void deleteReview(int reviewId) {
        myReviewRepository.deleteById(reviewId);
    }
    
    @Override
    @Transactional // 트랜잭션 관리
    public MyReviewDTO updateReview(int reviewId, MyReviewDTO updatedReviewDTO) {
        Review existingReview = myReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 리뷰를 찾을 수 없습니다: " + reviewId));

        // 업데이트할 리뷰 정보 업데이트
        existingReview.setReviewText(updatedReviewDTO.getReviewText());
        existingReview.setRating(updatedReviewDTO.getRating());
        existingReview.setReviewModify(new Date()); // 수정 일시 업데이트

        // 수정된 리뷰를 리포지토리에 저장
        myReviewRepository.save(existingReview);

        // 업데이트된 리뷰 DTO로 변환하여 반환
        return convertToDTO(existingReview);
    }
}
