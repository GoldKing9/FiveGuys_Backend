package fiveguys.webide.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    RETRY_EMAIL_AUTH(HttpStatus.BAD_REQUEST,"이메일 인증을 시도해 주세요.")
    ;

    private HttpStatus status;
    private String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
