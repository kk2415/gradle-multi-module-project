package com.together.community.controller.dto;

import com.together.community.domain.member.Authority;
import com.together.community.domain.member.Gender;
import com.together.community.domain.member.Member;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class MemberJoinForm {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{2,}$", message = "이메일은 2자리 이상, 영문자/숫자만 입력하세요")
    private String emailId;

    @NotNull
//    @Pattern(regexp = "^[a-zA-Z0-9]^[.]")
    private String emailDomain;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$", message = "비밀번호는 6자리이상, 영문자/숫자만 입력하세요")
    private String password;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,}$", message = "이름은 2자리 이상, 한글만 입력하세요")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Pattern(regexp = "^[0-9]{6,}$", message = "생년월일은 6자리 이상, 숫자만 입력하세요")
    private String birthday;

    @NotNull
    @Pattern(regexp = "^0\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 형식이 아닙니다.")
    private String phone;

    private Authority authority;

    public Member toEntity() {
        Member member = new Member();
        member.setEmailId(this.emailId);
        member.setEmailDomain(this.emailDomain);
        member.setPassword(this.password);
        member.setName(this.name);
        member.setGender(this.gender);
        member.setBirthday(this.birthday);
        member.setPhone(this.phone);
        member.setAuthority(Authority.NORMAL);
        member.setDateCreated(LocalDateTime.now());
        return member;
    }

}
