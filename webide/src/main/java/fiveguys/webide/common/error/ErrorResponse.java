package fiveguys.webide.common.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String status;
    private String message;

    public ErrorResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
