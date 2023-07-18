package dblab.sharing_flatform.repository.review;

import dblab.sharing_flatform.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r join fetch r.member rm where rm.username =:username")
    List<Review> findAllByMember(@Param("username") String username);
}
