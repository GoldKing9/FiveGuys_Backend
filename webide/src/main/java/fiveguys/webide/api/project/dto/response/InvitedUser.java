package fiveguys.webide.api.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @ToString
public class InvitedUser {
    private Long usreId;
    private String userNickname;
}