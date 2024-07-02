package com.lec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lec.entity.Member;
import com.lec.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class LoginService {
	
    @Autowired
    MemberRepository memberRepository;
	
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
    public Member authenticateUser(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        System.out.println("일반 로그인 멤버 정보 : " + member);
        if (member != null) {
            // 일반 로그인
            if (passwordEncoder.matches(password, member.getPassword())) {
                return member;
            }
        }
        return null;
    }

    public Member authenticateSocialUser(String email, String provider) {
        return memberRepository.findByEmailAndProvider(email, provider);
    }
	
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    
    //아이디 찾기 시
    public Member findByName(String nickname) {
    	return memberRepository.findByNickname(nickname);
    }
}

// 혹시 몰라 로직 하나 뺌 --> && member.getProvider() == null

