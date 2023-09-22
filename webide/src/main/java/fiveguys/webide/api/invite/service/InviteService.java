package fiveguys.webide.api.invite.service;

import fiveguys.webide.api.invite.dto.request.InviteRequest;
import fiveguys.webide.api.invite.dto.response.SearchUserInfoResponse;
import fiveguys.webide.api.invite.dto.response.UserInfoDto;
import fiveguys.webide.common.error.ErrorCode;
import fiveguys.webide.common.error.GlobalException;
import fiveguys.webide.domain.invite.Invite;
import fiveguys.webide.repository.invite.InviteRepository;
import fiveguys.webide.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public SearchUserInfoResponse search(String nickname, Pageable pageable) {
        PageImpl<UserInfoDto> userInfos = inviteRepository.searchNickname(nickname, pageable);
        return new SearchUserInfoResponse(userInfos);
    }

    @Transactional
    public void invite(Long projectId, InviteRequest request) {
        List<InviteRequest.UserIdDto> userDtos = request.getUsers();
        List<Invite> invites = userDtos.stream()
                .map(userDto -> new Invite(userDto.getUserId(), projectId))
                .toList();
        List<Long> userIds = extractUserId(userDtos);

        long userCount = userRepository.countByIdIn(userIds);

        validateExistUser(userIds, userCount);

        inviteRepository.saveAll(invites);
    }

    @Transactional
    public void deport(Long projectId, InviteRequest request) {
        List<InviteRequest.UserIdDto> userDtos = request.getUsers();

        for (InviteRequest.UserIdDto userDto : userDtos) {
            inviteRepository.deleteByProjectIdAndUserId(projectId, userDto.getUserId());
        }
    }

    private static void validateExistUser(List<Long> userIds, long userCount) {
        if (userCount != userIds.size()) {
            throw new GlobalException(ErrorCode.NON_EXIST_USER);
        }
    }

    private static List<Long> extractUserId(List<InviteRequest.UserIdDto> userDtos) {
        return userDtos.stream()
                .map(InviteRequest.UserIdDto::getUserId)
                .toList();
    }
}
