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
    _UNSUPPORTED_MEDIA_TYPE(UNSUPPORTED_MEDIA_TYPE, "C004", "지원하지 않는 Http Media PendingType 입니다"),
    _INVALID_REQUEST_PARAMETER(BAD_REQUEST, "C005", "유효하지 않은 Request Parameter 입니다"),
    CANNOT_CREATE_RECOMMEND(INTERNAL_SERVER_ERROR, "C006", "추천사 작성에 실패했습니다"),
    CANNOT_CREATE_RECOMMEND_REQUEST(INTERNAL_SERVER_ERROR, "C007", "추천사 요청에 실패했습니다"),
    NOT_MULTIPART_HEADER(BAD_REQUEST, "C008", "Multipart 헤더가 아닙니다"),
    AMAZON_ACCESS_DENIED(FORBIDDEN, "C009", "Amazon S3 접근이 거부되었습니다"),


    /* DB 관련 오류 */
    CANNOT_CREATE_TUPLE(INTERNAL_SERVER_ERROR, "DB000", "새로운 인스턴스 생성을 실패했습니다"),
    CANNOT_UPDATE_TUPLE(INTERNAL_SERVER_ERROR, "DB001", "인스턴스 업데이트에 실패했습니다"),

    /* 알림 관련 오류 */
    CANNOT_SEND_PUSH_NOTIFICATION(INTERNAL_SERVER_ERROR, "NOTICE000", "푸시 알림 전송에 실패했습니다"),

    /* Auth 관련 오류 */
    NO_TOKEN(UNAUTHORIZED, "AUTH000", "토큰이 존재하지 않습니다"),
    EXPIRED_TOKEN(UNAUTHORIZED, "AUTH001", "만료된 엑세스 토큰입니다"),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "AUTH002", "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(UNAUTHORIZED, "AUTH003", "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "AUTH004", "만료된 리프레시 토큰입니다"),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "AUTH005", "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "AUTH006", "로그인이 필요합니다"),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH007", "로그아웃 된 사용자입니다"),
    FORBIDDEN_USER(FORBIDDEN, "AUTH008", "권한이 없는 유저입니다"),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "AUTH009", "지원하지 않는 토큰입니다"),
    INVALID_SIGNATURE(UNAUTHORIZED, "AUTH010", "잘못된 JWT 서명입니다"),
    MISMATCH_VERIFICATION_CODE(UNAUTHORIZED, "AUTH011", "인증번호가 일치하지 않습니다"),
    EXPIRED_VERIFICATION_CODE(UNAUTHORIZED, "AUTH012", "인증번호가 만료되었습니다"),
    INVALID_USER_TOKEN(UNAUTHORIZED, "AUTH013", "서버에 토큰과 일치하는 정보가 없습니다"),

    LOGIN_FAILED(UNAUTHORIZED, "AUTH014", "로그인에 실패했습니다"),


    /* User 관련 오류 */
    CANNOT_FOLLOW_MYSELF(BAD_REQUEST, "U001", "자기 자신은 팔로우 할 수 없습니다"),
    USER_ALREADY_EXIST(BAD_REQUEST, "U002","이미 가입된 유저입니다"),
    USER_NOT_FOUND(NOT_FOUND, "U003","해당 유저 정보를 찾을 수 없습니다"),
    NOT_FOLLOW(NOT_FOUND, "U004","팔로우 중이지 않습니다"),
    DUPLICATE_PENDING_REQUEST(BAD_REQUEST, "U005","동일한 종류의 심사 요청은 한 번만 보낼 수 있습니다"),
    RANDOM_USER_NOT_FOUND(NOT_FOUND, "U006","추천할 수 있는 유저가 더 이상 없습니다"),
    FORBIDDEN_PROFILE(FORBIDDEN, "U007","해당 유저 프로필에 대한 접근 권한이 없습니다"),
    USER_NOT_SIGNED_UP(NOT_FOUND, "U008","정회원으로 가입된 유저가 아닙니다"),
    USER_ALREADY_LOGGED_IN(BAD_REQUEST, "U009","이미 로그인 상태입니다"),
    USER_ALREADY_LOGGED_OUT(BAD_REQUEST, "U010","이미 로그아웃 상태입니다"),


    /* Recommend 관련 오류 */
    RECOMMEND_NOT_FOUND(NOT_FOUND, "R000","추천사 정보를 찾을 수 없습니다"),
    RECOMMEND_ALREADY_EXIST(BAD_REQUEST, "R001","추천사를 같은 사람에게 중복으로 보낼 수 없습니다"),
    CANNOT_RECOMMEND_MYSELF(BAD_REQUEST, "R002","자기 자신은 추천할 수 없습니다"),
    RECOMMEND_SENDER_ALREADY_EXIST(BAD_REQUEST, "R003","유저 추천사는 한 번만 작성할 수 있습니다"),
    RECOMMEND_RECEIVER_NOT_EXIST(BAD_REQUEST, "R004","추천 받을 사람이 존재하지 않습니다"),
    RECOMMEND_REQUEST_ALREADY_EXIST(BAD_REQUEST, "R005","추천사 요청은 한 번만 보낼 수 있습니다"),
    RECOMMEND_NOT_RECEIVED(FORBIDDEN, "R006","가입하기 위해서는 먼저 추천사를 받아야 합니다"),
    RECOMMEND_SENDER_UNAUTHORIZED(UNAUTHORIZED, "R007","추천인의 인증이 아직 완료되지 않았습니다"),
    RECOMMEND_SENDER_NOT_EXIST(BAD_REQUEST, "R008","추천인 정보가 올바르지 않습니다"),


    /* Match 오류 */
    MATCH_NOT_FOUND(NOT_FOUND, "MATCH000", "매칭 정보를 찾을 수 없습니다"),
    FORBIDDEN_MATCH(FORBIDDEN, "MATCH001", "해당 매칭 정보에 대한 접근 권한이 없습니다"),
    MATCH_ALREADY_OPEN(BAD_REQUEST, "MATCH002", "이미 번호 오픈권을 사용했습니다"),
    MATCH_ALREADY_ACCEPTED(BAD_REQUEST, "MATCH002", "이미 호감을 수락했습니다"),
    BAD_MATCH_STATUS(BAD_REQUEST, "MATCH003", "매칭 상태 정보가 유효하지 않습니다"),

    /* Validation 오류 */
    PARAMETER_NOT_VALID(BAD_REQUEST, "P000", "인자가 유효하지 않습니다"),

    /* Database 관련 오류 */
    DUPLICATE_RESOURCE(CONFLICT, "D001", "데이터가 이미 존재합니다"),

    /* 이미지 관련 오류 */
    INVALID_FILE_EXTENSION(BAD_REQUEST, "FILE000", "잘못된 파일 확장자명입니다"),
    FILE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "FILE001", "파일 업로드에 실패했습니다"),

    /* 가입 대기 관련 */
    PENDING_NOT_FOUND(NOT_FOUND, "PEND000", "요청 대기 정보를 찾을 수 없습니다"),
    PENDING_ALREADY_PROCESSED(BAD_REQUEST, "PEND001", "이미 처리 완료된 요청입니다"),


    /* 카드 관련 */
    ACTIVE_CARD_NOT_FOUND(NOT_FOUND, "CARD000", "유효한 추천 카드를 찾을 수 없습니다"),
    ACTIVE_CARD_ALREADY_EXIST(BAD_REQUEST, "CARD001", "추천 카드는 한 장씩만 받을 수 있습니다"),
    CARD_LIMIT_EXCEED(BAD_REQUEST, "CARD002", "일일 추천 카드 한도를 초과했습니다"),


    /* 포인트 관련 */
    POINT_NOT_ENOUGH(BAD_REQUEST, "POINT000", "포인트가 부족합니다");

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String detail;
}