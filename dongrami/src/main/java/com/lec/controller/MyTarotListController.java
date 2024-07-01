package com.lec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lec.dto.MyTarotListDTO;
import com.lec.service.MyTarotListService;

@RestController
public class MyTarotListController {

    @Autowired
    private MyTarotListService myTarotListService;

    @GetMapping("/my-tarot-list")
    public List<MyTarotListDTO> getMyTarotList() {
        return myTarotListService.getAllMyTarotList();
    }
    

    @DeleteMapping("/delete-comment")
    public ResponseEntity<String> deleteSelectedComments(@RequestBody List<Integer> resultIds) {
        try {
            myTarotListService.deleteSavedResults(resultIds);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("댓글 삭제 중 오류가 발생하였습니다.");
        }
    }
}