package dblab.sharing_platform.repository.review;

import dblab.sharing_platform.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long>, QReviewRepository {

    @Query("select count(r) from Review r where r.writer.id =:memberId")
    Long countByMemberId(@Param("memberId") Long memberId);
}
