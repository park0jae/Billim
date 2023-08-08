package dblab.sharing_platform.repository.image;

import dblab.sharing_platform.domain.image.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
