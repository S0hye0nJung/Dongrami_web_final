package com.lec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lec.dto.MyReviewDTO;
import com.lec.entity.Review;
import com.lec.repository.MyReviewRepository;
import com.lec.service.MyReviewService;

@Controller
@RequestMapping("/reviews")
public class MyReviewController {

    private final MyReviewService myReviewService;
    private final MyReviewRepository myReviewRepository;

    @Autowired
    public MyReviewController(MyReviewService myReviewService, MyReviewRepository myReviewRepository) {
        this.myReviewService = myReviewService;
        this.myReviewRepository = myReviewRepository;
    }

    @GetMapping()
    public String getAllReviews(Model model) {
        List<MyReviewDTO> reviews = myReviewService.getAllReviewDTOs();
        model.addAttribute("reviews", reviews);
        return "myreview";  // HTML 파일의 이름과 일치해야 합니다
    }
    

//    @GetMapping
//    public ResponseEntity<List<MyReviewDTO>> getAllReviews() {
//        List<MyReviewDTO> reviews = myReviewService.getAllReviewDTOs();
//        return new ResponseEntity<>(reviews, HttpStatus.OK);
//    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable("reviewId") int reviewId) {
        myReviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping("/{reviewId}")
    public ResponseEntity<MyReviewDTO> updateReview(@PathVariable("reviewId") int reviewId, @RequestBody MyReviewDTO updatedReviewDTO) {
        MyReviewDTO updatedReview = myReviewService.updateReview(reviewId, updatedReviewDTO);
        return ResponseEntity.ok(updatedReview);
    }
    
    
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalReviewCount() {
        long totalCount = myReviewRepository.count();
        return ResponseEntity.ok(totalCount);
    }
}
