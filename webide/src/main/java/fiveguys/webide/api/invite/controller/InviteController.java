package fiveguys.webide.api.invite.controller;

import fiveguys.webide.api.invite.dto.request.InviteRequest;
import fiveguys.webide.api.invite.dto.response.SearchUserInfoResponse;
import fiveguys.webide.api.invite.service.InviteService;
import fiveguys.webide.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InviteController {
    private final InviteService inviteService;

    @GetMapping("/invite")
    public ResponseDto<SearchUserInfoResponse> search(@RequestParam String nickname, Pageable pageable) {
        return ResponseDto.success("유저 검색 성공", inviteService.search(nickname, pageable));
    }

    @PostMapping("/repo/{projectId}/invite")
    public ResponseDto<Void> invite(@PathVariable Long projectId, @RequestBody InviteRequest request) {
        inviteService.invite(projectId,request);
        return ResponseDto.success("초대 성공", null);
    }

    @DeleteMapping("/repo/{projectId}/invite")
    public ResponseDto<Void> deport(@PathVariable Long projectId,@RequestBody InviteRequest request) {
        inviteService.deport(projectId, request);
        return ResponseDto.success("추방 성공", null);
    }
}
