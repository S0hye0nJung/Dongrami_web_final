package com.lec.dto;

import java.util.Date;

import com.lec.entity.Review;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Data
public class ReviewDTO {
	
	
	private int reviewId;

    private int rating;

    private String reviewText;

    private String userId;

    private int subcategoryId;

    private int resultId;
    
    public Review toEntity() {
    	return Review.builder()
    			.reviewId(reviewId)
    			.rating(rating)
    			.reviewText(reviewText)
    			.build();
    }

}
