package fiveguys.webide.api.user.dto.request;

import fiveguys.webide.domain.user.User;
import fiveguys.webide.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequest {
    @Email(message = "이메일 형식에 맞지 않습니다")
    @NotBlank(message = "이메일은 필수 입력사항 입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력사항 입니다")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,12}$", message = "비밀번호는 8~12자의 숫자와 대소문자로만 구성되어야 합니다")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력사항 입니다")
    private String nickname;

    public User toEntity(String password) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(UserRole.USER)
                .build();
    }

}
