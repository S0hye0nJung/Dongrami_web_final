package com.lec.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RedingsDTO {
	
	private int readingId;
	private String readingKeyword;
	private String readingDescription;
    private int subcategoryId;

}
