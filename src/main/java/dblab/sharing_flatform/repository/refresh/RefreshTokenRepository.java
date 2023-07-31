package dblab.sharing_flatform.repository.refresh;

import dblab.sharing_flatform.domain.refresh.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUsername(String username);

    Optional<RefreshToken> findByToken(String token);
}
