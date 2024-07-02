package com.lec.service;

import java.text.ParseException;
import java.util.*;

import com.lec.dto.MemberDTO;
import com.lec.entity.Member;

public interface MemberService {
	
	Member join(MemberDTO memberDTO)throws ParseException;
	Date getCurrentDate() throws ParseException;
	String generateString();
	String generateRandomString(String source, int length, Random random);
	List<Member> findByNickname(String nickname);
	public boolean findPassword(String email);
}
