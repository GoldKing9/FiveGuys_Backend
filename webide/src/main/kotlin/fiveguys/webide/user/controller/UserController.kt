package fiveguys.webide.user.controller

import fiveguys.webide.common.aop.Trace
import fiveguys.webide.user.dto.request.SignupRequest
import fiveguys.webide.user.dto.request.LoginRequest
import fiveguys.webide.user.dto.response.LoginUserResponse
import fiveguys.webide.user.service.UserService
import fiveguys.webide.common.dto.ResponseDto
import fiveguys.webide.config.auth.LoginUser
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    @Trace
    fun signup(@Valid @RequestBody request: SignupRequest): ResponseDto<Void> {
        userService.signup(request)
        return ResponseDto.success("회원가입 성공", null)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseDto<LoginUserResponse> {
        return ResponseDto.success("로그인에 성공했습니다.", userService.login(request))
    }

    @GetMapping("/test")
    fun test(@AuthenticationPrincipal loginUser: LoginUser?): LoginUser? {
        println("ddddddddddddddddddddddddddddddddddd")
        return loginUser
    }

}
