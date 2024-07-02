package com.lec.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lec.Impl.ResultServiceImpl;
import com.lec.dto.WebReadingDTO;
import com.lec.dto.SavedResultDTO;
import com.lec.entity.Member;
import com.lec.entity.Card;
import com.lec.entity.SavedResult;
import com.lec.entity.WebReading;
import com.lec.service.SavedResultService;
import com.lec.repository.MemberRepository;
import com.lec.repository.CardRepository;
import com.lec.repository.WebReadingRepository;

@Controller
public class ResultController {
	
	@Autowired
	ResultServiceImpl resultService;
	
	@Autowired
	SavedResultService savedResultService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	CardRepository cardRepository;
	
	@Autowired
	WebReadingRepository webReadingRepository;

	@GetMapping("/result_three")
    public String showResult(@RequestParam("subcategory_id") int subcategoryId, Model model) {
        WebReadingDTO webReading = resultService.getReading(subcategoryId);
        System.out.println("세 개의 해석 : " + webReading + "타입 : " + webReading.getClass().getName());
        model.addAttribute("reading", webReading);
        return "result_three";
    }
    
    @GetMapping("/result_one")
    public String showOne(@RequestParam("subcategory_id") int subcategoryId, Model model) {
        List<WebReadingDTO> webReading = resultService.getOneCardReadings(subcategoryId);
        System.out.println("한 개의 해석 : " + webReading+ "타입 : " + webReading.getClass().getName());
        model.addAttribute("webReading", webReading);
        return "result_one";
    }

    @PostMapping("/result/saveResult")
    @ResponseBody
    public ResponseEntity<?> saveResult(@RequestBody SavedResultDTO savedResultDTO) {
        try {
            // 클라이언트로부터 받은 데이터를 로그로 출력
            System.out.println("Received SavedResultDTO: " + savedResultDTO);

            Member member = memberRepository.findById(savedResultDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Card card1 = cardRepository.findById(savedResultDTO.getCardId1()).orElseThrow(() -> new RuntimeException("Card1 not found"));
            Card card2 = cardRepository.findById(savedResultDTO.getCardId2()).orElseThrow(() -> new RuntimeException("Card2 not found"));
            Card card3 = cardRepository.findById(savedResultDTO.getCardId3()).orElseThrow(() -> new RuntimeException("Card3 not found"));
            WebReading webReading = webReadingRepository.findById(savedResultDTO.getWebReadingId()).orElseThrow(() -> new RuntimeException("WebReading not found"));
            
            SavedResult savedResult = savedResultDTO.toEntity(member, card1, card2, card3, webReading);
            savedResultService.saveResult(savedResult);

            // 저장된 결과를 로그로 출력
            System.out.println("Saved Result: " + savedResult);

            return ResponseEntity.ok(savedResult);
        } catch (Exception e) {
            System.err.println("Error saving result: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to save result: " + e.getMessage());
        }
    }
}
