package dblab.sharing_flatform.repository.trade;

import dblab.sharing_flatform.domain.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("select t from Trade t join fetch t.post tp where tp.id =:postId")
    Optional<Trade> findByPostId(@Param("postId") Long postId);
}