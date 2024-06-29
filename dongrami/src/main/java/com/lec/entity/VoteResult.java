package com.lec.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity(name="vote_result")
public class VoteResult {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_result_id", nullable = false)
    private int voteResultId;

    @Column(name = "question", nullable = false, length = 20)
    private String question;

    @Column(name = "option")
    private int option;

    @ManyToOne // 외래 키 관계 나타내기
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @Builder
	public VoteResult(int voteResultId, String question, Integer option, Vote vote, Member member) {
		super();
		this.voteResultId = voteResultId;
		this.question = question;
		this.option = option;
		this.vote = vote;
		this.member = member;
	}
    
    

}
