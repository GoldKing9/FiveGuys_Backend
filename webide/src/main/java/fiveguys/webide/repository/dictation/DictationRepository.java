package fiveguys.webide.repository.dictation;

import fiveguys.webide.domain.dictation.Dictation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DictationRepository extends JpaRepository<Dictation, Long> {
    Optional<Dictation> findByUserIdAndProjectIdAndPath(Long userId, Long projectId, String path);
}
