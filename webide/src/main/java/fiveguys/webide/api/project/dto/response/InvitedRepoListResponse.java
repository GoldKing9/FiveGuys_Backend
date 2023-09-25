package fiveguys.webide.api.project.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class InvitedRepoListResponse {
    private List<InvitedRepoInfo> repoList = new ArrayList<>();
    private Long repoCnt;
}
