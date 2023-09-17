package fiveguys.webide.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import fiveguys.webide.common.dto.ResponseDto
import fiveguys.webide.common.error.ErrorCode
import fiveguys.webide.common.error.ErrorResponse
import fiveguys.webide.config.auth.LoginUser
import fiveguys.webide.config.jwt.JwtProperties.Companion.ACCESS_TOKEN_EXPIRATION_TIME
import fiveguys.webide.config.jwt.JwtProperties.Companion.HEADER_STRING
import fiveguys.webide.config.jwt.JwtProperties.Companion.TOKEN_PREFIX
import fiveguys.webide.user.domain.User
import fiveguys.webide.user.domain.UserRole
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.security.Key
import java.util.*


@Component
class JwtUtils(
    @Value("\${jwt.app.jwtSecretKey}") secretKey: String
) {
    private val key: Key
    private val objectMapper = ObjectMapper()

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateAccessTokenFromLoginUser(loginUser: LoginUser): String {
        return Jwts.builder()
            .claim("id", loginUser.user.id)
            .claim("email", loginUser.user.email)
            .claim("nickname", loginUser.user.nickname)
            .claim("role", loginUser.user.role)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun parseJwtToken(request: HttpServletRequest): String? {
        var token = request.getHeader(HEADER_STRING)
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "")
        }
        return token
    }

    fun validationJwtToken(token: String, response: HttpServletResponse): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: MalformedJwtException) {
            setResponse(response, ErrorCode.INVALID_JWT_TOKEN, e)
        } catch (e: ExpiredJwtException) {
            setResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN, e)
        } catch (e: UnsupportedJwtException) {
            setResponse(response, ErrorCode.UNSUPPORTED_JWT_TOKEN, e)
        } catch (e: SignatureException) {
            setResponse(response, ErrorCode.INVALID_SIGNATURE_JWT_TOKEN, e)
        }
        return false
    }

    fun setResponse(response: HttpServletResponse, errorCode: ErrorCode, e: Exception) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        val fail: ResponseDto<ErrorResponse> = ResponseDto.fail(errorCode.status, errorCode.message)
        response.writer.write(objectMapper.writeValueAsString(fail))
    }

    fun verify(token: String): LoginUser {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val id = claims.get("id", Integer::class.java).toLong()
        val email = claims.get("email", String::class.java)
        val nickname = claims.get("nickname", String::class.java)
        val role = claims.get("role", String::class.java)
        val user = User(
            id = id,
            email = email,
            nickname = nickname,
            role = UserRole.valueOf(role),
            password = ""
        )
        return LoginUser(user)
    }

}
