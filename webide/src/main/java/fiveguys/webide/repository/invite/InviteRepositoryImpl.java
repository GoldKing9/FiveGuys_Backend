package fiveguys.webide.repository.invite;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fiveguys.webide.api.invite.dto.response.UserInfoDto;
import fiveguys.webide.api.project.dto.response.InvitedUser;
import fiveguys.webide.api.project.service.ProjectService;
import fiveguys.webide.domain.invite.QInvite;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static fiveguys.webide.domain.invite.QInvite.invite;
import static fiveguys.webide.domain.user.QUser.user;

@RequiredArgsConstructor
public class InviteRepositoryImpl implements InviteRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PageImpl<UserInfoDto> searchNickname(String nickname, Pageable pageable) {

        List<UserInfoDto> userInfos = jpaQueryFactory.select(Projections.constructor(UserInfoDto.class,
                        user.id,
                        user.nickname
                ))
                .from(user)
                .where(user.nickname.contains(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(user.count())
                .from(user)
                .where(user.nickname.contains(nickname))
                .fetchOne();
        return new PageImpl<>(userInfos, pageable, count);
    }

    @Override
    public List<InvitedUser> findInviteListByProjectId(Long projectId) {

        List<InvitedUser> findInvitedUser = jpaQueryFactory.select(Projections.constructor(InvitedUser.class, user.id, user.nickname))
                .from(invite, user)
                .where(invite.userId.eq(user.id), invite.projectId.eq(projectId))
                .fetch();

        return findInvitedUser;
    }
}
