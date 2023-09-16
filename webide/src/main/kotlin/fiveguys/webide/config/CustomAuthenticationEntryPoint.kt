package fiveguys.webide.config

import com.fasterxml.jackson.databind.ObjectMapper
import fiveguys.webide.common.dto.ResponseDto
import fiveguys.webide.common.error.ErrorCode
import fiveguys.webide.common.error.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component


@Component
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        if (authException is UsernameNotFoundException) {
            setResponse(response, ErrorCode.USERNAME_NOT_FOUND)
        } else if (authException is BadCredentialsException) {
            setResponse(response, ErrorCode.BAD_CREDENTIALS)

        }
    }

    private fun setResponse(response: HttpServletResponse?, errorCode: ErrorCode) {
        val objectMapper = ObjectMapper();
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.characterEncoding = "UTF-8"
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        val fail = ResponseDto.fail<ErrorResponse>(errorCode.status, errorCode.message)
        response?.writer?.write(objectMapper.writeValueAsString(fail));
    }
}
