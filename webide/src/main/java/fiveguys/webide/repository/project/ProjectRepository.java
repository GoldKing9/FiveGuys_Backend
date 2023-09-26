package fiveguys.webide.repository.project;

import fiveguys.webide.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByUserId(Long userId);

    List<Project> findAllByUserId(Long userId);
}
