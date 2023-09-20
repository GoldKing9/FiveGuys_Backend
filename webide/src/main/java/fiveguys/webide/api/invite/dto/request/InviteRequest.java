package fiveguys.webide.api.invite.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InviteRequest {
    private List<UserIdDto> users;

    public InviteRequest(List<UserIdDto> users) {
        this.users = users;
    }

    @Getter
    @NoArgsConstructor
    public static class UserIdDto {
        private Long userId;

        public UserIdDto(Long userId) {
            this.userId = userId;
        }
    }

}
