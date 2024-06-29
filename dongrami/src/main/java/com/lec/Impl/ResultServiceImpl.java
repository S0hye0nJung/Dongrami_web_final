package com.lec.Impl;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lec.dto.WebReadingDTO;
import com.lec.entity.Card;
import com.lec.entity.Subcategory;
import com.lec.entity.WebReading;
import com.lec.repository.CardRepository;
import com.lec.repository.ResultRepository;
import com.lec.repository.SubcategoryRepository;
  
@Service
public class ResultServiceImpl {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CardRepository cardRepository;

    public WebReadingDTO getReading(int subcategoryId) {
        List<ResultRepository.WebReadingProjection> projections = 
        		resultRepository.findRandomReadingsForSubcategory(subcategoryId);
        
        if (projections.size() != 3) {
            throw new RuntimeException("Expected 3 readings, but got " + projections.size());
        }

        Subcategory subcategory = subcategoryRepository.findById(subcategoryId)
            .orElseThrow(() -> new RuntimeException("Subcategory not found"));

        WebReadingDTO dto = new WebReadingDTO();
        dto.setSubcategory(subcategory);

        for (int i = 0; i < projections.size(); i++) {
        	ResultRepository.WebReadingProjection proj = projections.get(i);
            Card card = cardRepository.findById(proj.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found"));

            if (i == 0) {
                dto.setWebReadingId(proj.getWebReadingId());
                dto.setReading1(proj.getReading());
                dto.setReading1Title(proj.getReadingTitle());
                dto.setImageUrl1(proj.getCardImageUrl());
                dto.setCard(card);
            } else if (i == 1) {
                dto.setReading2(proj.getReading());
                dto.setReading2Title(proj.getReadingTitle());
                dto.setImageUrl2(proj.getCardImageUrl());
            } else {
                dto.setReading3(proj.getReading());
                dto.setReading3Title(proj.getReadingTitle());
                dto.setImageUrl3(proj.getCardImageUrl());
            }
        }

        return dto;
    }
    
	public List<WebReadingDTO> getOneCardReadings(int subcategoryId) {
		  List<Object[]> results = resultRepository.findOneCardReadings(subcategoryId);
		  return results.stream()
		       .map(this::mapToOneReadingDTO)
		       .collect(Collectors.toList());
	}
	  
	private WebReadingDTO mapToOneReadingDTO(Object[] result) {
		WebReadingDTO dto = new WebReadingDTO();
		dto.setWebReadingId((Integer) result[0]);
		dto.setReading1((String) result[1]);
		dto.setReading1Title((String) result[2]);
		
		Integer subcategoryId = (Integer) result[3];
		Integer cardId = (Integer) result[4];
		dto.setSubcategory(subcategoryRepository.findById(subcategoryId).orElse(null));
		dto.setCard(cardRepository.findById(cardId).orElse(null));
		dto.setImageUrl1((String) result[5]);
		return dto;
   }
    
//@Service
//public class ResultServiceImpl {
//
//    @Autowired
//    private ResultRepository resultRepository;
//    
//    @Autowired
//    private SubcategoryRepository subcategoryRepository;
//    
//    @Autowired
//    private CardRepository cardRepository;
//
//    public List<WebReadingDTO> getReadings(int subcategoryId) {
//        if (subcategoryId == 1 || subcategoryId == 2 || subcategoryId == 3 || 
//            subcategoryId == 4 || subcategoryId == 5 || subcategoryId == 10) {
//            return getRandomCardReadings(subcategoryId);
//        } else {
//            return getOneCardReadings(subcategoryId);
//        }
//    }
//  
//    public List<WebReadingDTO> getRandomCardReadings(int subcategoryId) {
//        List<Object[]> results = resultRepository.findRandomCardReadings(subcategoryId);
//        return results.stream()
//            .map(this::mapToWebReadingDTO)
//            .collect(Collectors.toList());
//    }
//
//    public List<WebReadingDTO> getOneCardReadings(int subcategoryId) {
//        List<Object[]> results = resultRepository.findOneCardReadings(subcategoryId);
//        return results.stream()
//             .map(this::mapToOneReadingDTO)
//             .collect(Collectors.toList());
//    }
//    
//    private WebReadingDTO mapToWebReadingDTO(Object[] result) {
//        WebReadingDTO dto = new WebReadingDTO();
//        dto.setWebReadingId((Integer) result[0]);
//        dto.setReading1((String) result[1]);
//        dto.setReading2((String) result[2]);
//        dto.setReading3((String) result[3]);
//        dto.setReading1Title((String) result[4]);
//        dto.setReading2Title((String) result[5]);
//        dto.setReading3Title((String) result[6]);
//        
//        Integer subcategoryId = (Integer) result[7];
//        Integer cardId = (Integer) result[8];
//        dto.setSubcategory(subcategoryRepository.findById(subcategoryId).orElse(null));
//        dto.setCard(cardRepository.findById(cardId).orElse(null));
//        dto.setImageUrl1((String) result[9]);
//        dto.setImageUrl2((String) result[10]);
//        dto.setImageUrl3((String) result[11]);
//        return dto;
//    }
//  
//    private WebReadingDTO mapToOneReadingDTO(Object[] result) {
//        WebReadingDTO dto = new WebReadingDTO();
//        dto.setWebReadingId((Integer) result[0]);
//        dto.setReading1((String) result[1]);
//        dto.setReading1Title((String) result[2]);
//        
//        Integer subcategoryId = (Integer) result[3];
//        Integer cardId = (Integer) result[4];
//        dto.setSubcategory(subcategoryRepository.findById(subcategoryId).orElse(null));
//        dto.setCard(cardRepository.findById(cardId).orElse(null));
//        dto.setImageUrl1((String) result[5]);
//        return dto;
//    }
}





