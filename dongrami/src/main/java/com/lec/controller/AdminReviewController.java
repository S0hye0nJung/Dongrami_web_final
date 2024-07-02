package com.lec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lec.dto.AdminReviewDto;
import com.lec.service.AdminReviewService;

@RestController
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    @Autowired
    private AdminReviewService adminReviewService;

    @GetMapping
    public List<AdminReviewDto> getAllReviews() {
        return adminReviewService.getAllReviews();
    }

    @GetMapping("/count")
    public long getReviewCount() {
        return adminReviewService.getReviewCount();
    }
    
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") int reviewId) {
        try {
            adminReviewService.deleteReview(reviewId);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete review");
        }
    }

    @GetMapping("/example")
    public String exampleMethod(@RequestParam("param1") int param1) {
        return "Value: " + param1;
    }
}
