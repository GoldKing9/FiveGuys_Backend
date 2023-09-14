package fiveguys.webide.common.error

import fiveguys.webide.common.dto.ResponseDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.ArrayList

@RestControllerAdvice
class GlobalControllerAdvice {
    val log = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(e: GlobalException): ResponseEntity<*> {
        log.warn("error message {}",e.errorCode.message, e);
        return ResponseEntity.status(e.errorCode.httpStatus)
            .body(ResponseDto.fail<ErrorResponse>(e.errorCode.status, e.errorCode.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validException(e: MethodArgumentNotValidException): ResponseEntity<*> {
        log.warn("error message {}",e.message, e);
        val fieldErrors = e.fieldErrors
        val errors: ArrayList<ResponseDto<ErrorResponse>> = ArrayList()
        for (fieldError in fieldErrors) {
            errors.add(ResponseDto.fail<ErrorResponse>(fieldError.field, fieldError.defaultMessage ?: null))
        }
        return ResponseEntity.status(e.statusCode)
            .body(errors)
    }

}
