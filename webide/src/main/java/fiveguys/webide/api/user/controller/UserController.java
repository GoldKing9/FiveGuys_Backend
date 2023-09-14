package fiveguys.webide.api.user.controller;

import fiveguys.webide.api.user.dto.request.LoginRequest;
import fiveguys.webide.api.user.dto.request.SignupRequest;
import fiveguys.webide.api.user.dto.request.TokenRefreshRequest;
import fiveguys.webide.api.user.dto.response.LoginUserResponse;
import fiveguys.webide.api.user.service.UserService;
import fiveguys.webide.common.dto.ResponseDto;
import fiveguys.webide.config.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseDto<Void> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseDto.success("회원가입 성공", null);
    }

    @PostMapping("/login")
    public ResponseDto<LoginUserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseDto.success("로그인에 성공했습니다.", userService.login(request));
    }

    @PostMapping("/reissue")
    public ResponseDto<LoginUserResponse> reissue(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseDto.success("토큰 재발행을 성공했습니다.", userService.reissue(request));
    }

    @GetMapping("/test")
    public LoginUser test(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser;
    }
}
