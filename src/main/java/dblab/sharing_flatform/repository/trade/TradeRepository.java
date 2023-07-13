package dblab.sharing_flatform.repository.trade;

import dblab.sharing_flatform.domain.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
