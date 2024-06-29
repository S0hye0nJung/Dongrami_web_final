package com.lec.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity(name="member")
public class Member {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;  

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;
    @Column(name = "birth_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Column(name = "create_date", nullable = false, updatable = false, columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "modify_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date modifyDate;
    @Column(name = "gender", nullable = true)
    private String gender;
    @Column(name = "provider", nullable = true)
    private String provider;

    public Member(String userId) {
        super();
        this.userId = userId;
    }
  //DB에서 not null인 컬럼들(생성일자 빼고)
    public Member(String userId, String password, String email, String provider) {
        super();
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.provider = provider;
    }
    
    @Builder
	public Member(String userId, String email, String password, String nickname, String phoneNumber, Date birthDate,
			Date createDate, Date modifyDate, String gender, String provider) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.gender = gender;
		this.provider = provider;
	}  
    
    
}
