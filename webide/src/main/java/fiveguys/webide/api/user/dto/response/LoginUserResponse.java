package fiveguys.webide.api.user.dto.response;

import fiveguys.webide.config.auth.LoginUser;
import fiveguys.webide.domain.user.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUserResponse {
    private Long userId;
    private String nickname;
    private UserRole role;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginUserResponse(LoginUser loginUser, String accessToken, String refreshToken) {
        this.userId = loginUser.getUser().getId();
        this.nickname = loginUser.getUser().getNickname();
        this.role = loginUser.getUser().getRole();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
