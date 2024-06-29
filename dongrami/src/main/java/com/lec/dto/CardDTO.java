package com.lec.dto;

import com.lec.entity.Card;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CardDTO {
	
	@NotNull
	private int cardId;
    
    @NotNull
    private String cardName;

    private String imageUrl;

    private int readingId;
    
    
    public Card toEntity() {
    	return Card.builder()
    			.cardId(cardId)
    			.cardName(cardName)
    			.imageUrl(imageUrl)
    			.build();
    }

}
