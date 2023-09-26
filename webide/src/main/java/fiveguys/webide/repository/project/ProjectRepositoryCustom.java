package fiveguys.webide.repository.project;

import fiveguys.webide.domain.project.Project;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {
    PageImpl<Project> findProjectListByUserId(Long userId, Pageable pageable);

}
