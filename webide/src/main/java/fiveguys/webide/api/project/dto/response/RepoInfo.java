package fiveguys.webide.api.project.dto.response;

import fiveguys.webide.api.project.service.ProjectService;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder @Getter
public class RepoInfo {
    private Long repoId;
    private String repoName;
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean bookmark;
    List<InvitedUser> invitedUser = new ArrayList<>();
    private Long invitedUserCnt;
}
