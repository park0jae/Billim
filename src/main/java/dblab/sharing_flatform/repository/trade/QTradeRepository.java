package dblab.sharing_flatform.repository.trade;

import dblab.sharing_flatform.dto.trade.TradeDto;
import dblab.sharing_flatform.dto.trade.TradePagingCondition;
import org.springframework.data.domain.Page;

public interface QTradeRepository {

    Page<TradeDto> findAllByCond(TradePagingCondition cond);

    Page<TradeDto> findAllByMyRend(TradePagingCondition cond);

    Page<TradeDto> findAllByMyBorrow(TradePagingCondition cond);
}
