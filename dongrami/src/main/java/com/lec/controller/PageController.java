package com.lec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/comments")
    public String commentsPage() {
        return "comments";
    }
    
    @GetMapping("/result")
    public String resultPage() {
        return "result";
    }
    
    @GetMapping("/review")
    public String reviewPage() {
        return "review";
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
    
<<<<<<< HEAD
    @GetMapping("/adminreview")
    public String adminReview() {
    	return "adminreview";
    }
=======
    @GetMapping("/find_Id")
    public String findId() {
    	return "find_Id";
    }
    
    @GetMapping("/find_Pwd")
    public String findPwd() {
    	return "find_Pwd";
    }
    
    
>>>>>>> 2482ef29cc52414c8a8fb2410eb00ee0d745fc33
}