package fiveguys.webide.repository.dictation;

import fiveguys.webide.domain.dictation.Dictation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictationRepository extends JpaRepository<Dictation, Long> {
}
