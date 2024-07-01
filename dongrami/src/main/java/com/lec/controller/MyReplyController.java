package com.lec.controller;

import com.lec.dto.MyReplyDTO;
import com.lec.service.MyReplyService;
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

    @GetMapping("/myreply")
    public String getUserReplies(@RequestParam("userId") String userId, Model model) {
        List<MyReplyDTO> replies = myReplyService.getRepliesByUserId(userId);
        model.addAttribute("replies", replies);
        return "reply"; // 템플릿 파일 이름을 reply로 반환
    }
}
