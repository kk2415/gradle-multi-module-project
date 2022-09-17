package com.levelup.api.controller.v1.member;

import com.levelup.api.service.dto.member.MemberDto;
import com.levelup.api.controller.v1.dto.response.member.MemberResponse;
import com.levelup.api.service.MemberService;
import com.levelup.api.controller.v1.dto.request.member.MemberRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Tag(name = "회원가입 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SignUpApiController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponse> signUp(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid MemberRequest request)
    {
        MemberDto dto = memberService.save(request.toDto());

        log.info("회원가입 성공 = url : {}, 이메일 : {}, 본명 : {}", httpServletRequest.getRequestURL(), request.getEmail(), request.getName());

        return ResponseEntity.ok().body(MemberResponse.from(dto));
    }
}