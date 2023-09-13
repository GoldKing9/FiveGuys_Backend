package fiveguys.webide.api.user.service;

import fiveguys.webide.api.user.dto.request.LoginRequest;
import fiveguys.webide.api.user.dto.request.SignupRequest;
import fiveguys.webide.api.user.dto.request.TokenRefreshRequest;
import fiveguys.webide.api.user.dto.response.LoginUserResponse;
import fiveguys.webide.common.error.GlobalException;
import fiveguys.webide.config.auth.LoginUser;
import fiveguys.webide.config.jwt.JwtUtils;
import fiveguys.webide.domain.redis.RefreshToken;
import fiveguys.webide.domain.user.User;
import fiveguys.webide.repository.redis.RefreshTokenRepository;
import fiveguys.webide.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static fiveguys.webide.common.error.ErrorCode.EXPIRED_REFRESH_TOKEN;
import static fiveguys.webide.common.error.ErrorCode.RETRY_EMAIL_AUTH;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignupRequest request) {
        if (!request.getIsValid()) {
            throw new GlobalException(RETRY_EMAIL_AUTH);
        }
        User user = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public LoginUserResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(loginUser);
        RefreshToken refreshToken = RefreshToken.builder()
                .loginUser(loginUser)
                .refreshToken(UUID.randomUUID().toString())
                .build();
        refreshTokenRepository.save(refreshToken);
        return LoginUserResponse.builder()
                .loginUser(loginUser)
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public LoginUserResponse reissue(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GlobalException(EXPIRED_REFRESH_TOKEN));
        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(findRefreshToken.getLoginUser());
        return LoginUserResponse.builder()
                .loginUser(findRefreshToken.getLoginUser())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
