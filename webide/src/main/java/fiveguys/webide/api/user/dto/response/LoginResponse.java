package fiveguys.webide.api.user.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {
    private Long userId;
    private String nickname;
    private String role;
    private String accessToken;
    private String refreshToken;
}
