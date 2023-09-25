package fiveguys.webide.api.project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class InvitedRepoList {
    private List<InvitedRepoInfo> repoList = new ArrayList<>();
    private Long repoCnt;
}
