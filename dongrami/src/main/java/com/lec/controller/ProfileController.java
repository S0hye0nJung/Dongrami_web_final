package com.lec.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.entity.Member;
import com.lec.repository.MemberRepository;

@Controller
public class ProfileController {
	
    private final MemberRepository memberRepository;

    @Autowired
    public ProfileController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 프로필 수정 페이지로 이동
    @GetMapping("/edit/{memberId}")
    public String showEditProfilePage(@PathVariable("memberId") String memberId, Model model) {
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            // 회원을 찾을 수 없는 경우 처리
            // 예: 에러 페이지로 리다이렉트 또는 적절한 메시지 출력 후 처리
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }

        model.addAttribute("member", member);
        return "editprofile"; // editprofile.html 페이지로 이동
    }

    // 프로필 수정 처리
    @PostMapping("/edit/{memberId}")
    public String processEditProfile(@PathVariable("memberId") String memberId,
                                     @ModelAttribute("member") Member updatedMember) {
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            // 회원을 찾을 수 없는 경우 처리
            // 예: 에러 페이지로 리다이렉트 또는 적절한 메시지 출력 후 처리
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }

        // 업데이트할 정보 설정
        member.setNickname(updatedMember.getNickname());
        member.setBirthDate(updatedMember.getBirthDate());
        member.setPhoneNumber(updatedMember.getPhoneNumber());

        memberRepository.save(member); // 수정된 회원 정보 저장

        // 수정 완료 후 프로필 보기 페이지로 리다이렉트
        return "redirect:/profile/view/" + memberId;
    }

    // 프로필 보기 페이지 (추가적으로 필요한 경우 구현)
    @GetMapping("/view/{memberId}")
    public String viewProfilePage(@PathVariable("memberId") String memberId, Model model) {
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            // 회원을 찾을 수 없는 경우 처리
            // 예: 에러 페이지로 리다이렉트 또는 적절한 메시지 출력 후 처리
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }

        model.addAttribute("member", member);
        return "viewprofile"; // viewprofile.html 페이지로 이동
    }
}