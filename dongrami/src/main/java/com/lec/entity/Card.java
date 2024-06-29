package com.lec.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "cards")
public class Card {

    @Id
    @Column(name = "card_id")
    private int cardId;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "reading_id", referencedColumnName = "reading_id", nullable = false)
    private Readings reading;

    @Builder
	public Card(int cardId, String cardName, String imageUrl, Readings reading) {
		super();
		this.cardId = cardId;
		this.cardName = cardName;
		this.imageUrl = imageUrl;
		this.reading = reading;
	}
    
    

}
