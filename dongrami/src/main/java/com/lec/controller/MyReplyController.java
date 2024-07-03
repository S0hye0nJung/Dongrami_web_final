package com.lec.controller;

import com.lec.dto.MyReplyDTO;
import com.lec.entity.Member;
import com.lec.service.MyReplyService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MyReplyController {

    @Autowired
    private MyReplyService myReplyService;
    
    @GetMapping("/reply")
    public String getUserReplies(HttpSession session, Model model) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        String userId = loggedInUser.getUserId(); // 또는 사용자 ID를 가져오는 적절한 메서드
        List<MyReplyDTO> replies = myReplyService.getRepliesByUserId(userId);
        model.addAttribute("replies", replies);
        return "reply";
    }

//    @GetMapping("/reply")
//    public String getUserReplies(@RequestParam("userId") String userId, Model model) {
//        List<MyReplyDTO> replies = myReplyService.getRepliesByUserId(userId);
//        model.addAttribute("replies", replies);
//        return "reply"; // 템플릿 파일 이름을 reply로 반환
//    }
}
