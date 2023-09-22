package fiveguys.webide.api.dictation.controller;

import fiveguys.webide.api.dictation.dto.request.DictationRequest;
import fiveguys.webide.api.dictation.dto.response.DictationResponse;
import fiveguys.webide.api.dictation.service.DictationService;
import fiveguys.webide.common.dto.ResponseDto;
import fiveguys.webide.config.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repo/{projectId}/file/dictation/{*path}")
public class DictationController {
    private final DictationService dictationService;

    @PostMapping
    public ResponseDto<Void> saveDictation(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long projectId,
            @PathVariable String path,
            @RequestBody DictationRequest request
    ) {
        dictationService.saveDictation(loginUser.getUser().getId(), projectId, path, request);
        return ResponseDto.success("받아쓰기 저장 성공", null);
    }

    @GetMapping
    public ResponseDto<DictationResponse> getDictation(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long projectId,
            @PathVariable String path
    ) {
        return ResponseDto.success("받아쓰기 조회 성공", dictationService.getDictation(loginUser.getUser().getId(), projectId, path));
    }
}
