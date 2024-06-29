package com.lec.dto;

import java.util.Date;

import com.lec.entity.SavedResult;

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
    
    public SavedResult toEntity() {
    	return SavedResult.builder()
    			.resultId(resultId)
    			.position1(position1)
    			.position2(position2)
    			.position3(position3)
    			.build();
    }

}
