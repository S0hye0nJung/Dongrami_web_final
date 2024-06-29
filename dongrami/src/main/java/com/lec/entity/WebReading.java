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
@Entity(name="web_reading")
public class WebReading {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="web_reading_id", nullable = false)
    private int webReadingId;

    @Column(name="reading1", columnDefinition = "text")

    private String reading1;

    @Column(name = "reading2", columnDefinition = "text")
    private String reading2;

    @Column(name = "reading3", columnDefinition = "text")
    private String reading3;

    @Column(name = "reading1_title")
    private String reading1Title;

    @Column(name = "reading2_title")
    private String reading2Title;

    @Column(name = "reading3_title")
    private String reading3Title;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id", nullable = false)
    private Card card;

    @Builder
	public WebReading(int webReadingId, String reading1, String reading2, String reading3, String reading1Title,
			String reading2Title, String reading3Title, Subcategory subcategory, Card card) {
		super();
		this.webReadingId = webReadingId;
		this.reading1 = reading1;
		this.reading2 = reading2;
		this.reading3 = reading3;
		this.reading1Title = reading1Title;
		this.reading2Title = reading2Title;
		this.reading3Title = reading3Title;
		this.subcategory = subcategory;
		this.card = card;
	}
    
    

}
