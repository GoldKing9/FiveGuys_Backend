package fiveguys.webide.api.user.service;

import fiveguys.webide.api.user.dto.request.LoginRequest;
import fiveguys.webide.api.user.dto.request.SignupRequest;
import fiveguys.webide.api.user.dto.response.LoginResponse;
import fiveguys.webide.common.error.ErrorCode;
import fiveguys.webide.common.error.GlobalException;
import fiveguys.webide.domain.user.User;
import fiveguys.webide.domain.user.UserRole;
import fiveguys.webide.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static fiveguys.webide.common.error.ErrorCode.RETRY_EMAIL_AUTH;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup(SignupRequest request) {
        if (!request.isValid()) {
            throw new GlobalException(RETRY_EMAIL_AUTH);
        }
        User user = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return null;
    }
}
