package com.lec.controller;

import com.lec.dto.AllReviewDTO;
import com.lec.entity.Member;
import com.lec.entity.Review;
import com.lec.entity.Subcategory;
import com.lec.entity.SavedResult;
import com.lec.service.AllReviewService;
import com.lec.service.SubcategoryService;
import com.lec.repository.MemberRepository;
import com.lec.repository.SavedResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/allreview")
public class AllReviewController {

    private static final Logger logger = LoggerFactory.getLogger(AllReviewController.class);

    @Autowired
    private AllReviewService allReviewService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubcategoryService subcategoryService;

    @Autowired
    private SavedResultRepository savedResultRepository;

    @PostMapping("/submit")
    public ResponseEntity<Review> submitReview(@RequestBody AllReviewDTO reviewDTO) {
        logger.debug("Received reviewDTO: {}", reviewDTO);

        if (reviewDTO.getUserId() == null) {
            logger.error("User ID is null in reviewDTO: {}", reviewDTO);
            return ResponseEntity.badRequest().body(null);
        }

        Member member = memberRepository.findById(reviewDTO.getUserId()).orElse(null);
        if (member == null) {
            logger.error("Member not found for user ID: {}", reviewDTO.getUserId());
            return ResponseEntity.badRequest().build();
        }

        Optional<Subcategory> subcategoryOptional = subcategoryService.findById(reviewDTO.getSubcategoryId());
        if (!subcategoryOptional.isPresent()) {
            logger.error("Subcategory not found for ID: {}", reviewDTO.getSubcategoryId());
            return ResponseEntity.badRequest().build();
        }

        Subcategory subcategory = subcategoryOptional.get();
        SavedResult savedResult = null;

        if (reviewDTO.getResultId() != null) {
            savedResult = savedResultRepository.findById(reviewDTO.getResultId()).orElse(null);
            if (savedResult == null) {
                logger.error("SavedResult not found for ID: {}", reviewDTO.getResultId());
                return ResponseEntity.badRequest().build();
            }
        }

        Review review = Review.builder()
                .rating(reviewDTO.getRating())
                .reviewText(reviewDTO.getReviewText())
                .reviewCreate(new Date())
                .reviewModify(new Date())
                .member(member)
                .subcategory(subcategory)
                .savedResult(savedResult) // null 또는 올바른 savedResult 설정
                .build();

        Review savedReview = allReviewService.saveReview(review);
        logger.debug("Review saved: {}", savedReview);

        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/json")
    public ResponseEntity<List<AllReviewDTO>> getAllReviewsJson() {
        List<AllReviewDTO> reviews = allReviewService.getAllReviewsWithNicknames();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping
    public String getAllReviews(Model model) {
        List<AllReviewDTO> reviews = allReviewService.getAllReviewsWithNicknames();
        model.addAttribute("reviews", reviews);
        return "allreview"; // Thymeleaf 템플릿 이름
    }

    // 추가된 메서드
    @GetMapping("/subcategory/{id}/bubbleSlackName")
    @ResponseBody
    public ResponseEntity<?> getBubbleSlackName(@PathVariable("id") int id) {
        logger.debug("Received request for subcategory ID: {}", id);
        Optional<Subcategory> subcategoryOptional = subcategoryService.findById(id);
        if (subcategoryOptional.isPresent()) {
            Subcategory subcategory = subcategoryOptional.get();
            logger.debug("Found subcategory: {}", subcategory);
            return ResponseEntity.ok().body(Map.of("bubbleSlackName", subcategory.getBubble_slack_name()));
        } else {
            logger.error("Subcategory not found for ID: {}", id);
            return ResponseEntity.status(404).body("Subcategory not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable("id") int id, @RequestBody AllReviewDTO reviewDTO) {
        logger.debug("Received update request for review ID: {}", id);

        Optional<Review> reviewOptional = allReviewService.getReviewById(id);
        if (!reviewOptional.isPresent()) {
            logger.error("Review not found for ID: {}", id);
            return ResponseEntity.status(404).body(null);
        }

        Review review = reviewOptional.get();
        review.setRating(reviewDTO.getRating());
        review.setReviewText(reviewDTO.getReviewText());
        review.setReviewModify(new Date());

        allReviewService.saveReview(review);
        logger.debug("Review updated: {}", review);

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") int id) {
        logger.debug("Received delete request for review ID: {}", id);

        Optional<Review> reviewOptional = allReviewService.getReviewById(id);
        if (!reviewOptional.isPresent()) {
            logger.error("Review not found for ID: {}", id);
            return ResponseEntity.status(404).build();
        }

        allReviewService.deleteReview(id);
        logger.debug("Review deleted for ID: {}", id);

        return ResponseEntity.ok().build();
    }
}
