package com.lec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity(name="maincategories")
public class Maincategories {
	
	@Id
	@Column(name = "maincategory_id", nullable = false)
	private int maincategory_id;
	
	@Column(name = "maincategory_name", nullable = false)
	private String maincategory_name;

	@Builder
	public Maincategories(int maincategory_id, String maincategory_name) {
		super();
		this.maincategory_id = maincategory_id;
		this.maincategory_name = maincategory_name;
	}
	
	
}
