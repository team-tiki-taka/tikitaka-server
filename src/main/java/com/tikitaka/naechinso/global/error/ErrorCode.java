package com.tikitaka.naechinso.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/** 에러 코드를 관리하기 위한 Enum 입니다 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 공통 오류 */
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "C000", "서버 에러, 관리자에게 문의 바랍니다"),
    _BAD_REQUEST(BAD_REQUEST, "C001", "잘못된 요청입니다"),
    _UNAUTHORIZED(UNAUTHORIZED, "C002", "권한이 없습니다"),

    _METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "C003", "지원하지 않는 Http Method 입니다"),
    CANNOT_CREATE_RECOMMEND(INTERNAL_SERVER_ERROR, "C004", "추천사 작성에 실패했습니다"),
    CANNOT_CREATE_RECOMMEND_REQUEST(INTERNAL_SERVER_ERROR, "C005", "추천사 요청에 실패했습니다"),


    /* Auth 관련 오류 */
    NO_TOKEN(UNAUTHORIZED, "AUTH000", "토큰이 존재하지 않습니다"),
    EXPIRED_TOKEN(UNAUTHORIZED, "AUTH001", "만료된 엑세스 토큰입니다"),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "AUTH002", "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(UNAUTHORIZED, "AUTH003", "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "AUTH004", "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "AUTH005", "로그인이 필요합니다"),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH006", "로그아웃 된 사용자입니다"),
    FORBIDDEN_USER(FORBIDDEN, "AUTH007", "권한이 없는 유저입니다"),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "AUTH008", "지원하지 않는 토큰입니다"),
    INVALID_SIGNATURE(UNAUTHORIZED, "AUTH009", "잘못된 JWT 서명입니다"),
    MISMATCH_VERIFICATION_CODE(UNAUTHORIZED, "AUTH010", "인증번호가 일치하지 않습니다"),
    EXPIRED_VERIFICATION_CODE(UNAUTHORIZED, "AUTH011", "인증번호가 만료되었습니다"),

    LOGIN_FAILED(UNAUTHORIZED, "AUTH012", "로그인에 실패했습니다"),


    /* User 관련 오류 */
    CANNOT_FOLLOW_MYSELF(BAD_REQUEST, "U001", "자기 자신은 팔로우 할 수 없습니다"),
    USER_ALREADY_EXIST(BAD_REQUEST, "U002","이미 가입된 유저입니다"),
    USER_NOT_FOUND(NOT_FOUND, "U003","해당 유저 정보를 찾을 수 없습니다"),
    NOT_FOLLOW(NOT_FOUND, "U004","팔로우 중이지 않습니다"),


    /* Recommend 관련 오류 */
    RECOMMEND_NOT_FOUND(NOT_FOUND, "R000","해당 추천사 정보를 찾을 수 없습니다"),
    RECOMMEND_ALREADY_EXIST(BAD_REQUEST, "R001","추천사를 같은 사람에게 중복으로 보낼 수 없습니다"),
    CANNOT_RECOMMEND_MYSELF(BAD_REQUEST, "R002","자기 자신은 추천할 수 없습니다"),
    RECOMMEND_SENDER_ALREADY_EXIST(BAD_REQUEST, "R003","유저 추천사는 한 번만 작성할 수 있습니다"),
    RECOMMEND_RECEIVER_NOT_EXIST(BAD_REQUEST, "R004","추천 받을 사람이 존재하지 않습니다"),
    RECOMMEND_REQUEST_ALREADY_EXIST(BAD_REQUEST, "R005","추천사 요청은 한 번만 보낼 수 있습니다"),

    /* Validation 오류 */
    PARAMETER_NOT_VALID(BAD_REQUEST, "P000", "인자가 유효하지 않습니다"),

    /* Database 관련 오류 */
    DUPLICATE_RESOURCE(CONFLICT, "D001", "데이터가 이미 존재합니다"),



    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String detail;
}