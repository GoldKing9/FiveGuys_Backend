package fiveguys.webide.api.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder @Getter @AllArgsConstructor
public class InvitedRepoInfo {
    private Long repoId;
    private String repoName;
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean bookmark;
    private String hostUser;
}
