package fiveguys.webide.repository.invite;

import fiveguys.webide.domain.invite.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long>, InviteRepositoryCustom {
    void deleteByProjectIdAndUserId(Long projectId, Long userId);
}
