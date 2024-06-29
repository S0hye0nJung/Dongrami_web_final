package com.lec.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", nullable = false)
    private int voteId;

    @Column(name = "question", nullable = false, length = 255)
    private String question;

    @Column(name = "vote_image", nullable = false, length = 50)
    private String voteImage;

    @Column(name = "option1", nullable = false, length = 20)
    private String option1;

    @Column(name = "option2", nullable = false, length = 20)
    private String option2;

    @Column(name = "option1_count")
    private Integer option1Count;

    @Column(name = "option2_count")
    private Integer option2Count;

    @Column(name = "vote_create")
    private Date voteCreate;

    @Column(name = "vote_end")
    private Date voteEnd;
    
    
    
    @Builder
	public Vote(int voteId, String question, String voteImage, String option1, String option2, Integer option1Count,
			Integer option2Count, Date voteCreate, Date voteEnd) {
		super();
		this.voteId = voteId;
		this.question = question;
		this.voteImage = voteImage;
		this.option1 = option1;
		this.option2 = option2;
		this.option1Count = option1Count;
		this.option2Count = option2Count;
		this.voteCreate = voteCreate;
		this.voteEnd = voteEnd;
	}

    

}
