package fiveguys.webide.repository.invite;

import fiveguys.webide.api.invite.dto.response.UserInfoDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InviteRepositoryCustom {
    PageImpl<UserInfoDto> searchNickname(String nickname, Pageable pageable);
}
