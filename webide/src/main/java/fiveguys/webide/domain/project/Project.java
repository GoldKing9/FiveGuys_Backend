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
    private String repoName;
    private String projectName;
    private boolean bookmark;

    @Builder
    public Project(Long userId, String repoName, String projectName, boolean bookmark) {
        this.userId = userId;
        this.repoName = repoName;
        this.projectName = projectName;
        this.bookmark = bookmark;
    }

    public void changeRepoName(String repoName) {
        this.repoName = repoName;
    }
    public void changeBookmark() {
        this.bookmark = !this.isBookmark();
    }
}
