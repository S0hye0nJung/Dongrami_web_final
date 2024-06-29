package com.lec.dto;

import java.util.Date;

import com.lec.entity.Vote;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class VoteDTO {
	
	 private int voteId;
	 private String question;
	 private String voteImage;
	 private String option1;
	 private String option2;
	 private int option1Count;
	 private int option2Count;
	 private Date voteCreate;
	 private Date voteEnd;
	 
	 public Vote toEntity() {
		 return Vote.builder()
				 .voteId(voteId)
				 .question(question)
				 .voteImage(voteImage)
				 .option1(option1)
				 .option2(option2)
				 .option1Count(option1Count)
				 .option2Count(option2Count)
				 .voteCreate(voteCreate)
				 .voteEnd(voteEnd)
				 .build();
	 }

}
