package fiveguys.webide.api.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class CreateRepoResponse {
    private Long repoId;
    private String projectName;
}
