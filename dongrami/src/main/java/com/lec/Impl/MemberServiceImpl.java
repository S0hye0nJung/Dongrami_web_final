package com.lec.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lec.dto.MemberDTO;
import com.lec.entity.Member;
import com.lec.repository.MemberRepository;
import com.lec.service.MemberService;


@Service
public class MemberServiceImpl implements MemberService{
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	public Date birthDay = null;
	
	@Autowired
	MemberRepository memberrepository;

	@Autowired
	EmailServiceImpl emailService;
	
	public Member join(MemberDTO memberDTO) throws ParseException {
		
		Member member = memberDTO.toEntity();
		
		String userId = generateString();
		Date createDate = getCurrentDate();
		
		if (memberDTO.getBirthYear() != null && 
				memberDTO.getBirthMonth() != null && 
						memberDTO.getBirthDay() != null) {
			    birthDay = getBirthDay(memberDTO.getBirthYear(), 
			    		memberDTO.getBirthMonth(), 
			    		memberDTO.getBirthDay());
			}
		
		member.setUserId(userId);

		member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
		member.setBirthDate(birthDay);
		member.setCreateDate(createDate);
		memberrepository.save(member);

		
		return member; //회원가입 완료 후 반환되는 템플릿 명
	}


	// 생성일자 구하는 로직
	public Date getCurrentDate() throws ParseException {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	String dateString = dateFormat.format(new Date());
	    	return dateFormat.parse(dateString);
	    }


	// 회원 아이디 - 영문자, 숫자, 영문자&숫자로 이루어진 10글자 반환 
    public String generateString() {
        Random random = new Random();
        String source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return generateRandomString(source, 10, random);
    }


	
	// 회원 아이디 - 실제로 10자의 랜덤한 String 생성
	public String generateRandomString(String source, int length, Random random) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(source.length());
            sb.append(source.charAt(randomIndex));
        }
        return sb.toString();
    }
	
	//DTO에서 받은 정보로 생년월일 구하기
		public Date getBirthDay(String year, String month, String day) throws ParseException {
			String birthday = year + "-" + month + "-" + day;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.parse(birthday);
		}


	// 아이디 찾기
	@Override
	public List<Member> findByNickname(String nickname) {
		return memberrepository.findAllByNickname(nickname);
	}


	// 비밀번호 찾기
	@Override
	public boolean findPassword(String email) {
		
		Member member = memberrepository.findByEmail(email);
        if (member != null) {
            String tempPassword = generateTempPassword();
            member.setPassword(passwordEncoder.encode(tempPassword));
            memberrepository.save(member);
            emailService.sendTempPassword(email, tempPassword);
            return true;
        }
        return false;
		
	}
	
	private String generateTempPassword() {
		Random random = new Random();
		return String.format("%08d", random.nextInt(100000000));
	}
	
	
}


