package com.lec.dto;

import com.lec.entity.Vote;
import com.lec.entity.VoteResult;
import com.lec.entity.WebReading;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class VoteResultDTO {
	
	private int voteResultId;
	private String question;
	private int option;
	private int voteId; //외래키
	
	public VoteResult toEntity() {
		return VoteResult.builder()
				.voteResultId(voteResultId)
				.question(question)
				.option(option)
				.build();
	}

}
