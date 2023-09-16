package fiveguys.webide.common.error

import fiveguys.webide.common.aop.ThreadLocalContextHolder
import fiveguys.webide.common.dto.ResponseDto
import fiveguys.webide.common.logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.ArrayList

@RestControllerAdvice
class GlobalControllerAdvice {
    val log = logger()

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(e: GlobalException): ResponseEntity<*> {
        val traceId = ThreadLocalContextHolder.getTraceId()
        log.error("ERROR TRACING_ID : {} ERROR MESSAGE : {}", traceId, e.errorCode.message, e)
        return ResponseEntity.status(e.errorCode.httpStatus)
            .body(ResponseDto.fail<ErrorResponse>(e.errorCode.status, e.errorCode.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validException(e: MethodArgumentNotValidException): ResponseEntity<*> {
        val traceId = ThreadLocalContextHolder.getTraceId()
        log.error("ERROR TRACING_ID : {} ERROR MESSAGE :{}", traceId, e.message, e)
        val fieldErrors = e.fieldErrors
        val errors: ArrayList<ResponseDto<ErrorResponse>> = ArrayList()
        for (fieldError in fieldErrors) {
            errors.add(ResponseDto.fail(fieldError.field, fieldError.defaultMessage ?: null))
        }
        return ResponseEntity.status(e.statusCode)
            .body(errors)
    }

}
