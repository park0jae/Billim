package dblab.sharing_flatform.repository.emailAuth;

import dblab.sharing_flatform.domain.emailAuth.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, String> {

    Optional<EmailAuth> findByEmailAndPurpose(String email, String purpose);

    boolean existsByEmail(String email);

    boolean existsByKey(String Key);

    void deleteByEmail(String email);
}
