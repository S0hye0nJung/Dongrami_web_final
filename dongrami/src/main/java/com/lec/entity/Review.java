package com.lec.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.ConstraintMode;


@Setter
@Getter
@NoArgsConstructor
@Table(name = "review")
@Entity(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 필드 설정
    @Column(name = "review_id")
    private int reviewId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "review_text", columnDefinition = "text")
    private String reviewText;

    @Column(name = "review_create", nullable = false)
    private Date reviewCreate;

    @Column(name = "review_modify")
    private Date reviewModify;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "subcatogory_id", referencedColumnName = "subcategory_id", nullable = false)
    private Subcategory subcategory;
    
    @ManyToOne
    @JoinColumn(name = "fk_result_id", 
                referencedColumnName = "result_id", 
                nullable = true, 
                foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SavedResult savedResult;

    @Builder
	public Review(int reviewId, int rating, String reviewText, Date reviewCreate, Date reviewModify, Member member,
			Subcategory subcategory, SavedResult savedResult) {
		super();
		this.reviewId = reviewId;
		this.rating = rating;
		this.reviewText = reviewText;
		this.reviewCreate = reviewCreate;
		this.reviewModify = reviewModify;
		this.member = member;
		this.subcategory = subcategory;
	}
}