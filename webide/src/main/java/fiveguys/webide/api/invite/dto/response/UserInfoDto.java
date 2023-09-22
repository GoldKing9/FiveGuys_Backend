package fiveguys.webide.api.invite.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String nickname;

    public UserInfoDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
