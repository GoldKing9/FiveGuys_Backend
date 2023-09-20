package fiveguys.webide.domain.project;

import fiveguys.webide.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String projectName;
    private boolean bookmark;

    @Builder
    public Project(Long userId, String projectName, boolean bookmark) {
        this.userId = userId;
        this.projectName = projectName;
        this.bookmark = bookmark;
    }
}
