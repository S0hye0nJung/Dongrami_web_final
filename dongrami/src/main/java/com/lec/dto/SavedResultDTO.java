package com.lec.dto;

import java.util.Date;

import com.lec.entity.Card;
import com.lec.entity.Member;
import com.lec.entity.SavedResult;
import com.lec.entity.WebReading;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SavedResultDTO {
	
	private int resultId;
    private Date resultDate;
    private String position1;
    private String position2;
    private String position3;
    private String userId;
    private int cardId1;
    private int cardId2;
    private int cardId3;
    private int webReadingId;
    
    public SavedResult toEntity(Member member, Card card1, Card card2, Card card3, WebReading webReading) {
    	return SavedResult.builder()
    			.resultId(resultId)
    			.resultDate(resultDate != null ? resultDate : new Date()) // resultDate가 null일 경우 현재 날짜로 설정
    			.position1(position1)
    			.position2(position2)
    			.position3(position3)
    			.member(member)
    			.card1(card1)
    			.card2(card2)
    			.card3(card3)
    			.webReading(webReading)
    			.build();
    }
}
