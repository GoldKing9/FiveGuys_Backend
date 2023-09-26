package fiveguys.webide.repository.project;

import com.querydsl.jpa.impl.JPAQueryFactory;
import fiveguys.webide.domain.project.Project;
import fiveguys.webide.domain.project.QProject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static fiveguys.webide.domain.project.QProject.project;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public PageImpl<Project> findProjectListByUserId(Long userId, Pageable pageable) {

        List<Project> findProjectList = jpaQueryFactory.select(project)
                .from(project)
                .where(project.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(project.count())
                .from(project)
                .where(project.userId.eq(userId))
                .fetchOne();

        return new PageImpl<>(findProjectList, pageable, count);
    }
}
