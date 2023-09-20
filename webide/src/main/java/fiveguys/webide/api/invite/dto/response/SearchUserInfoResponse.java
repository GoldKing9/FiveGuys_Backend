package fiveguys.webide.api.invite.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchUserInfoResponse {
    private List<UserInfoDto> users;
    private int currentPage;
    private int totalPage;

    public SearchUserInfoResponse(PageImpl<UserInfoDto> userInfos) {
        this.users = userInfos.getContent();
        this.currentPage = userInfos.getNumber();
        this.totalPage = userInfos.getTotalPages();
    }
}
