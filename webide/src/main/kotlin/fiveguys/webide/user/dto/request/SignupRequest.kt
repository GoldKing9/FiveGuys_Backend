package fiveguys.webide.user.dto.request

import fiveguys.webide.user.domain.User
import fiveguys.webide.user.domain.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequest(
    @field:Email(message = "이메일 형식에 맞지 않습니다")
    @field:NotBlank(message = "이메일은 필수 입력사항 입니다")
    val email: String,
    @field:NotBlank(message = "비밀번호는 필수 입력사항 입니다")
    @field:Pattern(regexp = "^[a-zA-Z0-9]{8,12}$", message = "비밀번호는 8~12자의 숫자와 대소문자로만 구성되어야 합니다")
    val password: String,
    @field:NotBlank(message = "닉네임은 필수 입력사항 입니다")
    val nickname: String,
    val isValid: Boolean
){
    fun toEntity(password: String): User {
        return User(
            email,
            password,
            nickname,
            UserRole.USER
        )
    }
}

