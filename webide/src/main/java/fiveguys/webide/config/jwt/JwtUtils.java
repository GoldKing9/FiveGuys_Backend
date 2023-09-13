package fiveguys.webide.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiveguys.webide.common.dto.ResponseDto;
import fiveguys.webide.common.error.ErrorCode;
import fiveguys.webide.common.error.ErrorResponse;
import fiveguys.webide.config.auth.LoginUser;
import fiveguys.webide.domain.user.User;
import fiveguys.webide.domain.user.UserRole;
import fiveguys.webide.repository.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import static fiveguys.webide.common.error.ErrorCode.*;
import static fiveguys.webide.config.jwt.JwtProperties.*;

@Slf4j
@Component
public class JwtUtils {
    private final Key key;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtUtils(@Value("${jwt.app.jwtSecretKey}")String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessTokenFromLoginUser(LoginUser loginUser) {
        return Jwts.builder()
                .claim("id", loginUser.getUser().getId())
                .claim("email",loginUser.getUser().getEmail())
                .claim("nickname",loginUser.getUser().getNickname())
                .claim("role", loginUser.getUser().getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String parseJwtToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
        return token;
    }

    public boolean validationJwtToken(String token, HttpServletResponse response) throws IOException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            setResponse(response, INVALID_JWT_TOKEN, e);
        } catch (ExpiredJwtException e) {
            setResponse(response, EXPIRED_ACCESS_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            setResponse(response, UNSUPPORTED_JWT_TOKEN, e);
        }
        return false;
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode, Exception e) throws IOException {
        log.error("error message {}", errorCode.getMessage(), e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseDto<ErrorResponse> fail = ResponseDto.fail(errorCode.getStatus(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(fail));
    }

    public LoginUser verify(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long id = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        String nickname = claims.get("nickname", String.class);
        String role = claims.get("role", String.class);
        User user = User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .role(UserRole.valueOf(role))
                .build();
        return new LoginUser(user);
    }

}
