package fiveguys.webide.api.dictation.service;

import fiveguys.webide.api.dictation.dto.request.DictationRequest;
import fiveguys.webide.api.dictation.dto.response.DictationResponse;
import fiveguys.webide.common.error.ErrorCode;
import fiveguys.webide.common.error.GlobalException;
import fiveguys.webide.domain.dictation.Dictation;
import fiveguys.webide.repository.dictation.DictationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DictationService {
    private final DictationRepository dictationRepository;

    @Transactional(readOnly = true)
    public DictationResponse getDictation(Long userId, Long projectId, String path) {
        Dictation dictation = dictationRepository.findByUserIdAndProjectIdAndPath(userId, projectId, path).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_EXIST_DICTATION)
        );
        return new DictationResponse(dictation.getBody());
    }

    @Transactional
    public void saveDictation(Long userId, Long projectId, String path, DictationRequest request) {
        Dictation dictation = dictationRepository.findByUserIdAndProjectIdAndPath(userId, projectId, path)
                .orElse(
                        Dictation.builder()
                                .path(path)
                                .userId(userId)
                                .projectId(projectId)
                                .body(request.getBody())
                                .build()
                );
        dictation.update(request.getBody());
        dictationRepository.save(dictation);
    }
}
