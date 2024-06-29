package com.lec.dto;


import com.lec.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class MemberDTO {
	
	@NotBlank
    private String email;

	@NotBlank
    private String password;

	@NotBlank
    private String password2;

	@NotBlank
    private String nickname;

    private String phoneNumber;

    @NotBlank
    private String birthYear;

    @NotBlank
    private String birthMonth;

    @NotBlank
    private String birthDay;

    private String gender;
    
    public Member toEntity() {
      return Member.builder()
    		  .email(email)
    		  .nickname(nickname)
    		  .phoneNumber(phoneNumber)
    		  .gender(gender)
    		  .build();
    		  
    }

}
