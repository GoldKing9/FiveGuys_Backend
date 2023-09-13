package fiveguys.webide.common.dto;

import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private Integer status;
    private String message;
    private T data;

    public ResponseDto(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDto<T> success(int status, String message, T data) {
        return new ResponseDto<>(status, message, data);
    }

    public static <T> ResponseDto<T> fail(int status, String message) {
        return new ResponseDto<>(status, message, null);
    }

}
