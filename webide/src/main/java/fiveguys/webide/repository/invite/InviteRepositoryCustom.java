package fiveguys.webide.repository.invite;

import com.querydsl.core.QueryResults;
import fiveguys.webide.api.invite.dto.response.UserInfoDto;
import fiveguys.webide.api.project.dto.response.InvitedUser;
import fiveguys.webide.api.project.service.ProjectService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InviteRepositoryCustom {
    PageImpl<UserInfoDto> searchNickname(String nickname, Pageable pageable);

    List<InvitedUser> findInviteListByProjectId(Long projectId);
}
