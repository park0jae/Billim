package dblab.sharing_flatform.repository.review;

import dblab.sharing_flatform.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, QReviewRepository {

    @Query("select count(r) from Review r where r.writer.id =:memberId")
    Long countByMemberId(@Param("memberId") Long memberId);
}
