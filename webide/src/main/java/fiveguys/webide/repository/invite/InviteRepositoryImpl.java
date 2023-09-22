package fiveguys.webide.repository.invite;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fiveguys.webide.api.invite.dto.response.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
}
