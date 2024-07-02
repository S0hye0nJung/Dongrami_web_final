package com.lec.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AllReviewDTO {
    private int reviewId;
    private String userId;
    private String reviewText;
    private int subcategoryId;
    private int rating;
    private Integer resultId;
    private String nickname;
    private String bubbleSlackName;
    private Date reviewCreate;
    private Date reviewModify;

    // 기본 생성자
    public AllReviewDTO() {
    }

    // 모든 필드를 포함하는 생성자
    @JsonCreator
    public AllReviewDTO(
            @JsonProperty("reviewId") int reviewId,
            @JsonProperty("userId") String userId,
            @JsonProperty("reviewText") String reviewText,
            @JsonProperty("subcategoryId") int subcategoryId,
            @JsonProperty("rating") int rating,
            @JsonProperty("resultId") Integer resultId,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("bubbleSlackName") String bubbleSlackName,
            @JsonProperty("reviewCreate") Date reviewCreate,
            @JsonProperty("reviewModify") Date reviewModify) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.reviewText = reviewText;
        this.subcategoryId = subcategoryId;
        this.rating = rating;
        this.resultId = resultId;
        this.nickname = nickname;
        this.bubbleSlackName = bubbleSlackName;
        this.reviewCreate = reviewCreate;
        this.reviewModify = reviewModify;
    }

    // getters and setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBubbleSlackName() {
        return bubbleSlackName;
    }

    public void setBubbleSlackName(String bubbleSlackName) {
        this.bubbleSlackName = bubbleSlackName;
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
}
