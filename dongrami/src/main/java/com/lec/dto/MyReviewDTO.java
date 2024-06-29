package com.lec.dto;

import java.util.Date;

import com.lec.entity.Review;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MyReviewDTO {
	
    private int reviewId;
    private String mainCategoryName;
    private String reviewText;
    private Date reviewCreate;
    private int rating; // 추가된 rating 필드

    @Builder
    public MyReviewDTO(int reviewId, String mainCategoryName, String reviewText, Date reviewCreate, int rating) {
        this.reviewId = reviewId;
        this.mainCategoryName = mainCategoryName;
        this.reviewText = reviewText;
        this.reviewCreate = reviewCreate;
        this.rating = rating; // 빌더에도 rating 추가
    }
}
