package org.sparta.foodordermanagementservice.common.exeption;


import lombok.Getter;
import org.springframework.http.HttpStatus;


/* ErrorCode, message를 필요한 enum값으로 정의 */

@Getter
public enum ErrorCode {

    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "C001", "해당 자원이 존재하지 않습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "C002", "이미 존재하는 데이터입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "Internal Server Error"),
    ENTITY_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "db 저장 실패"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C011", "잘못된 요청입니다. (%s)"),

    /* 회원가입 */
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "S001", "중복된 아이디입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "S002", "중복된 닉네임입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "S003", "중복된 닉네임입니다."),
    WRONG_ROLE(HttpStatus.BAD_REQUEST, "S004", "회원가입은 고객과 가게 주인만 할 수 있습니다."),

    /*로그인*/
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "L001", "로그인이 필요합니다."),
    FAIL_LOGIN(HttpStatus.BAD_REQUEST, "L002", "로그인 실패"),

    /* 유저 */
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U001", "사용자가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String description;


    ErrorCode(HttpStatus status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}