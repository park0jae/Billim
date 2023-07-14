package dblab.sharing_flatform.repository.profileImage;

import dblab.sharing_flatform.domain.image.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
