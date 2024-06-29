package com.lec.Impl;

import com.lec.entity.Member;
import com.lec.entity.Review;
import com.lec.entity.Subcategory;
import com.lec.repository.AllReviewRepository;
import com.lec.repository.MemberRepository;
import com.lec.repository.SubcategoryRepository;
import com.lec.service.AllReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AllReviewServiceImpl implements AllReviewService {

    @Autowired
    private AllReviewRepository allReviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Review saveReview(Review review) {
        entityManager.persist(review); // Review 객체를 저장
        return review;
    }

    @Override
    public List<Review> getAllReviewsWithNicknames() {
        List<Review> reviews = allReviewRepository.findAll(); // 모든 리뷰를 조회

        // 리뷰에 대한 멤버의 닉네임 정보 및 subcategory의 bubble_slack_name 추가
        return reviews.stream()
                .peek(review -> {
                    Member member = memberRepository.findById(review.getMember().getUserId()).orElse(null);
                    if (member != null) {
                        review.getMember().setNickname(member.getNickname());
                    }
                    Subcategory subcategory = subcategoryRepository.findById(review.getSubcategory().getSubcategory_id()).orElse(null);
                    if (subcategory != null) {
                        review.getSubcategory().setBubble_slack_name(subcategory.getBubble_slack_name());
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Review> getReviewById(int id) {
        return allReviewRepository.findById(id);
    }

    @Override
    public void deleteReview(int id) {
        allReviewRepository.deleteById(id);
    }
}
