package com.lec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/reply")
    public String commentsPage() {
        return "myreply";
    }
    
    @GetMapping("/result")
    public String resultPage() {
        return "result";
    }
    
    @GetMapping("/review")
    public String reviewPage() {
        return "allreview";
    }
    
    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }
    
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/mainvote")
    public String mainvote() {
       return "mainvote";
    }
    @GetMapping("/vote")
    public String votePage() {
        return "votepage";
    }
    
    @GetMapping("/myreview")
    public String myReview() {
       return "myreview";
    }
    
    @GetMapping("/mytarotlist")
    public String myTarotList() {
       return "mytarotlist";
    }
    @GetMapping("/adminreview")
    public String adminReview() {
       return "adminreview";
    }

    @GetMapping("/find_Id")
    public String findId() {
       return "find_Id";
    }
    
    @GetMapping("/find_Pwd")
    public String findPwd() {
       return "find_Pwd";
    }
    
  
}