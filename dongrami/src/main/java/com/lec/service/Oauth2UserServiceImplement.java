package com.lec.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.entity.Member;
import com.lec.repository.MemberRepository;

@Service
public class Oauth2UserServiceImplement extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;
    private static final String DEFAULT_PASSWORD = "Passw0rd";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName(); // provider

        logUserInfo(oAuth2User); // 콘솔에 찍는 메서드 호출

        Member member = null;
        try {
            if (oauthClientName.equals("kakao")) {
                member = extractMemberInfoFromKakaoOAuth2(oAuth2User, oauthClientName); //카카오 로그인 시 사용자 정보 가져오기 위한 메서드 호출
            } else if (oauthClientName.equals("naver")) {
                member = extractMemberFromNaverOAuth2User(oAuth2User, oauthClientName); // 네이버 로그인 시 사용자 정보 가져오기 위한 메소드 호출
            } else {
            	member = extractMemberFromGoogleOAuth2User(oAuth2User, oauthClientName);
            	System.out.println("구글 로그인");
            }
        } catch (ParseException e) {
            // 예외 처리 로직 추가
            e.printStackTrace();
        }

     // db에 userId(email)이 없는 경우에만 저장하도록 구현
        Optional<Member> existingMember = memberRepository.findByUserId(member.getUserId());
        if (!existingMember.isPresent()) {
            memberRepository.save(member); // 실제로 저장
        }

        // OAuth2User 객체 반환
        return oAuth2User;
    }

    private void logUserInfo(OAuth2User oAuth2User) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes())); // 가져온 정보 콘솔에 찍어보기
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // kakao 로직 - 회원정보 가져오기
    private Member extractMemberInfoFromKakaoOAuth2(OAuth2User oAuth2User, String oauthClientName) throws ParseException {
        Map<String, Object> kakaoUserData = (Map<String, Object>) oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = getKakaoAccount(kakaoUserData);
        String email = getEmail(kakaoAccount);
        String nickname = getNickname(kakaoAccount); //이름 가져오기

        String connectedAt = (String) oAuth2User.getAttributes().get("connected_at");
        Date createDateKakao = getCreateDate(connectedAt);
        
        //id값 가져오기
        String user_id = oAuth2User.getAttributes().get("id").toString();
        
        // 멤버 객체 생성
        Member member = new Member(user_id, DEFAULT_PASSWORD, email, oauthClientName);
        member.setNickname(nickname);
        member.setCreateDate(createDateKakao);

        return member;
    }

    // kakao_account 사용자 정보가 담긴 맵 가져오기
    private Map<String, Object> getKakaoAccount(Map<String, Object> kakaoUserData) {
        return (Map<String, Object>) kakaoUserData.get("kakao_account");
    }

    // email 값 가져오기
    private String getEmail(Map<String, Object> kakaoAccount) {
        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
            return (String) kakaoAccount.get("email");
        }
        return null;
    }

    // nickname 값 가져오기
    private String getNickname(Map<String, Object> kakaoAccount) {
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        if (profile != null && profile.containsKey("nickname")) {
            return (String) profile.get("nickname");
        }
        return null;
    }
    
    // kakao 로직 - 가입일자(connected_at 이라는 json의 컬럼을 이용)
    private Date getCreateDate(String connectedDate) throws ParseException {
        if (connectedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(connectedDate);
        }
        return null;
    }
    
    // 네이버 로직 - 회원정보 가져오기
    private Member extractMemberFromNaverOAuth2User(OAuth2User oAuth2User, String oauthClientName) throws ParseException {
        Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");

        String user_id = responseMap.get("id").substring(0,10);
        String name = responseMap.get("name"); //이름
        String email = responseMap.get("email");
        String gender = responseMap.get("gender");
        String phoneNumber = responseMap.get("mobile");
        String nickname = responseMap.get("nickname");//별칭
        String birth = responseMap.get("birthday");
        String birthYear = responseMap.get("birthyear");

        Date birthDate = parseBirthDate(birth, birthYear);
        Date createDate = getCurrentDate(); // 현재 날짜를 yyyy-MM-dd 형식의 문자열로 가져옴
       
        // 필수 필드만 사용하여 기본 생성
        Member member = new Member(user_id, DEFAULT_PASSWORD, email, oauthClientName);

        // 선택 필드가 있는 경우에 따라 추가 설정
        if (gender != null) member.setGender(gender);
        if (phoneNumber != null) member.setPhoneNumber(phoneNumber);
        member.setNickname(nickname != null ? nickname : name); // nickname이 null인 경우 name을 사용
        if (birthDate != null) member.setBirthDate(birthDate);

        member.setCreateDate(createDate); // 실제 DB에서 not null인 생성일자

        return member;
    }

    // naver 로직 - 생년월일
    private Date parseBirthDate(String birthDateStr, String birthYearStr) throws ParseException {
        if (birthDateStr == null || birthDateStr.isEmpty()) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // birthYear가 제공되면, 해당 연도를 기존의 birthDate에 적용하여 새로운 Date 객체를 생성합니다.
        if (birthDateStr != null && !birthYearStr.isEmpty()) {
            String combinedDateStr = birthYearStr + "-" + birthDateStr;
            return dateFormat.parse(combinedDateStr);
        }

        // birthYear가 없는 경우, 9999 + birthDate를 반환합니다.
        if (birthYearStr == null && birthDateStr != null) {
            birthYearStr = "9999";
            String combinedDateStr = birthYearStr + "-" + birthDateStr;
            return dateFormat.parse(combinedDateStr);
        }

        return null;
    }
   
    // 구글 로직 - 회원정보 가져오기
    private Member extractMemberFromGoogleOAuth2User(OAuth2User oAuth2User, String oauthClientName) throws ParseException {
    	String user_id = oAuth2User.getAttributes().get("sub").toString().substring(0,10); //10글자만 짤라서 가져오기
    	String email = oAuth2User.getAttributes().get("email").toString();
    	String nickname = oAuth2User.getAttributes().get("given_name").toString();
    	
      	System.out.println("아이디 :" + user_id + "이메일 : " + email + "이름 : " + nickname );
    	
    	// 필수 필드만 사용하여 기본 생성
        Member member = new Member(user_id, DEFAULT_PASSWORD, email, oauthClientName);
        
        Date createDate = getCurrentDate();  
        member.setCreateDate(createDate); // 실제 DB에서 not null인 생성일자
        member.setNickname(nickname);
    	
    	return member;
    }
    
 // 네이버, 구글 로직 - 가입일자(현재 날짜 이용)
    public Date getCurrentDate() throws ParseException {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String dateString = dateFormat.format(new Date());
    	return dateFormat.parse(dateString);
    }
    
}