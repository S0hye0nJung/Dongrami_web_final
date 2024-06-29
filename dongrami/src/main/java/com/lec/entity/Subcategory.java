package com.lec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="subcategories")
public class Subcategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="subcategory_id", nullable = false)
	private int subcategory_id;
	
	@Column(name="subcategory_name", nullable = false)
	private String subcategory_name;
	
	@Column(name="count")
	private int count;
	
	@Column(name="bubble_slack_name")
	private String bubble_slack_name;
	
	@ManyToOne
    @JoinColumn(name = "maincategory_id", referencedColumnName = "maincategory_id", nullable = false)
    private Maincategories maincategory;


	@Builder
	public Subcategory(int subcategory_id, String subcategory_name, int count, String bubble_slack_name, Maincategories maincategory) {
		super();
		this.subcategory_id = subcategory_id;
		this.subcategory_name = subcategory_name;
		this.count = count;
		this.bubble_slack_name = bubble_slack_name;
		this.maincategory = maincategory;
	}

    
    public String getBubble_slack_name() {
        return this.bubble_slack_name;
    }


	
}
