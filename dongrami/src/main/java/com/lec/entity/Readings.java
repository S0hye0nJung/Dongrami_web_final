package com.lec.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "readings")
public class Readings {
	
	@Id
    @Column(name = "reading_id", nullable = false)
    private int readingId;

    @Column(name = "reading_keyword")
    private String readingKeyword;

    @Column(name = "reading_description")
    private String readingDescription;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    @Builder
	public Readings(int readingId, String readingKeyword, String readingDescription, Subcategory subcategory) {
		super();
		this.readingId = readingId;
		this.readingKeyword = readingKeyword;
		this.readingDescription = readingDescription;
		this.subcategory = subcategory;
	}
    
    

}
