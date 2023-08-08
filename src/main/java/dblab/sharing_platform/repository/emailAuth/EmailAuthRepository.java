package dblab.sharing_platform.repository.emailAuth;

import dblab.sharing_platform.domain.emailAuth.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, String> {

    Optional<EmailAuth> findByEmailAndPurpose(String email, String purpose);

    boolean existsByEmailAndPurpose(String email, String purpose);

    boolean existsByKey(String Key);

    void deleteByEmail(String email);
}
