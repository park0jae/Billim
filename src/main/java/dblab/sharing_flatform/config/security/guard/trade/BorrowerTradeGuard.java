package dblab.sharing_flatform.config.security.guard.trade;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BorrowerTradeGuard extends Guard {

    private final TradeRepository tradeRepository;
    @Override
    protected boolean isResourceOwner(Long id) {
        Long currentId = Long.valueOf(SecurityUtil.getCurrentUserId().orElseThrow(AccessDeniedException::new));
        return tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new).getBorrowerMember().getId().equals(currentId);

    }
}
