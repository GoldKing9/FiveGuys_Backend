package fiveguys.webide.common.error;

import fiveguys.webide.common.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity<?> handleGlobalException(GlobalException e) {
        log.warn("error message {}", e.getErrorCode().getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.fail(e.getErrorCode().getStatus(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> validException(MethodArgumentNotValidException e) {
        log.warn("error message {}", e.getMessage(), e);
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ResponseDto<ErrorResponse>> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            ResponseDto<ErrorResponse> fail = ResponseDto.fail(fieldError.getField(), fieldError.getDefaultMessage());
            errors.add(fail);
        }
        return ResponseEntity.status(e.getStatusCode())
                .body(errors);
    }
}
