package dblab.sharing_platform.repository.trade;

import dblab.sharing_platform.domain.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface TradeRepository extends JpaRepository<Trade, Long>, QTradeRepository{
    @Query("select t from Trade t join fetch t.post p where p.id =:postId")
    Optional<Trade> findByPostId(@Param("postId") Long postId);
    @Query("select t from Trade t join fetch t.review tr")
    Optional<Long> findReviewIdByTrade(@Param("reviewId") Long reviewId);
}
