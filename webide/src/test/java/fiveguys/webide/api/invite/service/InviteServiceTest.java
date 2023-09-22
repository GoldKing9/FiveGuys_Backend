package fiveguys.webide.api.invite.service;

import fiveguys.webide.IntegrationTestSupport;
import fiveguys.webide.api.invite.dto.request.InviteRequest;
import fiveguys.webide.api.invite.dto.response.SearchUserInfoResponse;
import fiveguys.webide.common.error.ErrorCode;
import fiveguys.webide.common.error.GlobalException;
import fiveguys.webide.domain.user.User;
import fiveguys.webide.repository.invite.InviteRepository;
import fiveguys.webide.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InviteServiceTest extends IntegrationTestSupport {
    @Autowired
    private InviteService inviteService;
    @Autowired
    private InviteRepository inviteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void setUP() {
        jdbcTemplate.execute("TRUNCATE TABLE user");
        jdbcTemplate.execute("TRUNCATE TABLE invite");
    }

    @DisplayName("닉네임 검색하기")
    @Test
    void search_nickname() {
        // given
        insertUsers();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        SearchUserInfoResponse result = inviteService.search("ab", pageable);

        // then
        assertThat(result.getUsers()).hasSize(1);
        assertThat(result.getUsers().get(0).getNickname()).isEqualTo("abc");
    }

    @DisplayName("초대하기")
    @Test
    void invite() {
        // given
        insertUsers();
        InviteRequest request = request();

        // when
        inviteService.invite(1L,request);

        // then
        int result = inviteRepository.findAll().size();
        assertThat(result).isEqualTo(2);
    }

    @DisplayName("탈퇴한 유저를 초대했을때 예외가 터진다.")
    @Test
    void non_exist_user_invite() {
        // given
        InviteRequest request = request();

        // when // then
        assertThatThrownBy(() -> inviteService.invite(1L, request))
                .isInstanceOf(GlobalException.class)
                .satisfies(ex ->
                        assertThat(((GlobalException) ex).getErrorCode()).isEqualTo(ErrorCode.NON_EXIST_USER)
                );

    }

    @DisplayName("추방하기")
    @Test
    void deport() {
        // given
        insertUsers();
        InviteRequest request = request();
        inviteService.invite(1L, request);

        // when
        inviteService.deport(1L, request);

        // then
        int result = inviteRepository.findAll().size();
        assertThat(result).isZero();
    }

    private void insertUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().nickname("aaa").build());
        users.add(User.builder().nickname("bbb").build());
        users.add(User.builder().nickname("abc").build());
        userRepository.saveAll(users);
    }

    private static InviteRequest request() {
        List<InviteRequest.UserIdDto> userIdDtos = new ArrayList<>();
        InviteRequest.UserIdDto userIdDto1 = new InviteRequest.UserIdDto(1L);
        InviteRequest.UserIdDto userIdDto2 = new InviteRequest.UserIdDto(2L);
        userIdDtos.add(userIdDto1);
        userIdDtos.add(userIdDto2);
        InviteRequest request = new InviteRequest(userIdDtos);
        return request;
    }
}
