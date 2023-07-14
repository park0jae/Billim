package dblab.sharing_flatform.repository.image;

import dblab.sharing_flatform.domain.image.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<PostImage, Long> {
}
