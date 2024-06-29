package com.lec.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyTarotListDTO {
    private int resultId;
    private Date resultDate;
    private String subcategoryName;
    private String maincategoryName;
}
