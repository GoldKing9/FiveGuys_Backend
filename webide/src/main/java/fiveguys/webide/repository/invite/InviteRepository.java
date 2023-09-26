package fiveguys.webide.repository.invite;

import fiveguys.webide.domain.invite.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long>, InviteRepositoryCustom {
    void deleteByProjectIdAndUserId(Long projectId, Long userId);

    List<Invite> findAllByProjectId(Long repoId);
}
