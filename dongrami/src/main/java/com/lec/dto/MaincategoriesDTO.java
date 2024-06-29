package com.lec.dto;

import com.lec.entity.Maincategories;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MaincategoriesDTO {
	
	private int maincategory_id;
	private String maincategory_name;
	
	public Maincategories toEntity() {
		return Maincategories.builder()
				.maincategory_id(maincategory_id)
				.maincategory_name(maincategory_name)
				.build();
	}

}
