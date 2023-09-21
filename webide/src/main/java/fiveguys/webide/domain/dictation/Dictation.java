package fiveguys.webide.domain.dictation;

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
public class Dictation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;
    private Long userId;
    private Long projectId;
    private String body;

    @Builder
    public Dictation(String path, Long userId, Long projectId, String body) {
        this.path = path;
        this.userId = userId;
        this.projectId = projectId;
        this.body = body;
    }

    public void update(String body) {
        this.body = body;
    }
}
