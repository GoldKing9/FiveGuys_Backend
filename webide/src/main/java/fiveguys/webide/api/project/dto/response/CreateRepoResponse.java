package fiveguys.webide.api.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.type.descriptor.java.LocalDateJavaType;

import java.time.LocalDateTime;

@AllArgsConstructor @Getter @Builder
public class CreateRepoResponse {
    private Long repoId;
    private String projectName;
    private String createdAt;
    private String updatedAt;
}
