package fiveguys.webide.api.user.controller;

import fiveguys.webide.api.user.dto.request.LoginRequest;
import fiveguys.webide.api.user.dto.request.SignupRequest;
import fiveguys.webide.api.user.dto.response.LoginResponse;
import fiveguys.webide.api.user.service.UserService;
import fiveguys.webide.common.dto.ResponseDto;
import fiveguys.webide.common.error.ErrorCode;
import fiveguys.webide.common.error.GlobalException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseDto<Void> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseDto.success(HttpStatus.OK.value(), "회원가입 성공", null);
    }

    @PostMapping("/login")
    public ResponseDto<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseDto.success(HttpStatus.OK.value(), "로그인에 성공했습니다.", userService.login(request));
    }

    @GetMapping("/{id}")
    public ResponseDto<Void> test(@PathVariable Long id) {
        if (id == 1) {
            throw new GlobalException(ErrorCode.RETRY_EMAIL_AUTH);
        }
        return ResponseDto.success(HttpStatus.OK.value(), "회원가입 성공", null);
    }
}
