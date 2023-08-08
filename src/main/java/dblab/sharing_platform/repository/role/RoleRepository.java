package dblab.sharing_platform.repository.role;

import dblab.sharing_platform.domain.role.Role;
import dblab.sharing_platform.domain.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
