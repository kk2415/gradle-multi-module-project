package com.levelup.api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    MEMBER_NOT_FOUND("존재하지 않는 회원입니다."),
    IMAGE_NOT_FOUND("이미지를 찾을 수 없습니다."),
    ARTICLE_NOT_FOUND("존재하지 않는 게시글입니다."),

    VOTE_DUPLICATION("추천은 한 번만 가능합니다."),
    EMAIL_DUPLICATION("이미 존재하는 이메일입니다."),
    CHANNEL_MEMBER_DUPLICATION("이미 등록된 회원입니다."),

    SECURITY_CODE_EXPIRED("인증코드가 만료되었습니다."),
    NOT_MATCH_SECURITY_CODE("인증코드가 일치하지 않습니다."),

    NO_PLACE_CHANNEL("스터디/프로젝트 정원이 다 찼습니다."),

    BAD_REQUEST("잘못된 접근입니다");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}