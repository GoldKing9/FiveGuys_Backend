package fiveguys.webide.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USERNAME_NOT_FOUND(HttpStatus.UNAUTHORIZED,"USERNAME_NOT_FOUND", "계정이 존재하지 않습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "EXIST_EMAIL", "존재하는 이메일 입니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "EXIST_NICKNAME", "존재하는 닉네임 입니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "BAD_CREDENTIALS","비밀번호가 불일치 합니다."),
    RETRY_EMAIL_AUTH(HttpStatus.BAD_REQUEST, "RETRY_EMAIL_AUTH","이메일 인증을 시도해 주세요."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_JWT_TOKEN","JWT 토큰이 유효하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Token Expired","JWT 토큰이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"UNSUPPORTED_JWT_TOKEN", "지원하지 않는 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "EXPIRED_REFRESH_TOKEN","리프레시 토큰시간이 만료되었습니다. 다시 로그인 해주세요."),
    ;

    private HttpStatus httpStatus;
    private String status;
    private String message;

    ErrorCode(HttpStatus httpStatus, String status, String message) {
        this.httpStatus = httpStatus;
        this.status = status;
        this.message = message;
    }
}
