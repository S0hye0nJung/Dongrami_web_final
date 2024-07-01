package com.lec.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lec.dto.MemberDTO;
import com.lec.entity.Member;
import com.lec.service.MemberService;
import com.lec.service.Oauth2UserServiceImplement;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {

    @Autowired
    Oauth2UserServiceImplement userService; //소셜 로그인 서비스
    
    @Autowired
    MemberService memberService; // 일반 회원 가입 로그인
    
    @GetMapping("/signup")
    public String signup() {
    	return "signup";
    }
    
    
    @GetMapping("/find_id") 
    public String idFind() {
    	return "find_Id";
    }

    @GetMapping("/join")
    public String showJoinForm(Model model) {
        model.addAttribute("memberCreateForm", new MemberDTO());
        return "join";
    }

    @PostMapping("/join")
    @ResponseBody
    public Map<String, Object> join(@ModelAttribute MemberDTO memberCreateForm) throws ParseException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Member createdMember = memberService.join(memberCreateForm);
            System.out.println("회원가입 성공..");
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다!");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "회원가입 실패: " + e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("userId", loggedInUser.getUserId());
            System.out.println("사용자 아이디: " + loggedInUser.getUserId());
            model.addAttribute("email", session.getAttribute("email"));
            model.addAttribute("provider", session.getAttribute("provider"));
        } else {
            System.out.println("로그인하지 않은 사용자");
            return "index";
        }
        return "index";
    }

}
