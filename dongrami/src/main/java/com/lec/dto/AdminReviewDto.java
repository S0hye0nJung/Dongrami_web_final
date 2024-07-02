package com.lec.dto;

import java.util.Date;

import com.lec.entity.Review;

public class AdminReviewDto {
    private int reviewId;
    private int rating;
    private String reviewText;
    private Date reviewCreate;
    private Date reviewModify;
    private Date memberCreateDate;
    private String memberId;
    private String memberNickname;

    public AdminReviewDto() {
    }

    public AdminReviewDto(int reviewId, int rating, String reviewText, Date reviewCreate, Date reviewModify, Date memberCreateDate, String memberId, String memberNickname) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewCreate = reviewCreate;
        this.reviewModify = reviewModify;
        this.memberCreateDate = memberCreateDate;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
    }

    public AdminReviewDto(Review review) {
        this.reviewId = review.getReviewId();
        this.rating = review.getRating();
        this.reviewText = review.getReviewText();
        this.reviewCreate = review.getReviewCreate();
        this.reviewModify = review.getReviewModify();
        this.memberCreateDate = review.getMember().getCreateDate();  // 수정 필요
        this.memberId = review.getMember().getUserId();
        this.memberNickname = review.getMember().getNickname();
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Date getReviewCreate() {
        return reviewCreate;
    }

    public void setReviewCreate(Date reviewCreate) {
        this.reviewCreate = reviewCreate;
    }

    public Date getReviewModify() {
        return reviewModify;
    }

    public void setReviewModify(Date reviewModify) {
        this.reviewModify = reviewModify;
    }

    public Date getMemberCreateDate() {
        return memberCreateDate;
    }

    public void setMemberCreateDate(Date memberCreateDate) {
        this.memberCreateDate = memberCreateDate;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }
}
