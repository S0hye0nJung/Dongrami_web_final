package com.lec.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lec.dto.MyTarotListDTO;
import com.lec.entity.SavedResult;
import com.lec.repository.MyTarotListRepository;

@Service
public class MyTarotListService {


    @Autowired
    private MyTarotListRepository myTarotListRepository;

    public List<MyTarotListDTO> getAllMyTarotList() {
        List<SavedResult> savedResults = myTarotListRepository.findAllWithDetails();
        return savedResults.stream().map(sr -> {
            String subcategoryName = sr.getWebReading().getSubcategory().getSubcategory_name();
            String maincategoryName = sr.getWebReading().getSubcategory().getMaincategory().getMaincategory_name();
            return new MyTarotListDTO(
                sr.getResultId(),
                sr.getResultDate(),
                subcategoryName != null ? subcategoryName : "", // null 체크
                maincategoryName != null ? maincategoryName : "" // null 체크
            );
        }).collect(Collectors.toList());
    }
    
    public void deleteComment(Integer commentId) {
        myTarotListRepository.deleteById(commentId);
    }
}
