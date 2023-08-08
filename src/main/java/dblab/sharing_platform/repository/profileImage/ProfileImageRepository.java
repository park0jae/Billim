package dblab.sharing_platform.repository.profileImage;

import dblab.sharing_platform.domain.image.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
