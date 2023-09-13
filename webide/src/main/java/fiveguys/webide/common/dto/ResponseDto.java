package fiveguys.webide.common.dto;

import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private String status;
    private String message;
    private T data;

    public ResponseDto(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>("Success", message, data);
    }

    public static <T> ResponseDto<T> fail(String status, String message) {
        return new ResponseDto<>(status, message, null);
    }

}
