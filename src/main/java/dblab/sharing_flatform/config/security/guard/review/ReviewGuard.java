package dblab.sharing_flatform.config.security.guard.review;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewGuard extends Guard {

    private final TradeRepository tradeRepository;
    @Override
    protected boolean isResourceOwner(Long id) {
        Trade trade = tradeRepository.findById(id).orElseThrow(GuardException::new);
        return Long.valueOf(SecurityUtil.getCurrentUserId().orElseThrow(GuardException::new)).equals(trade.getBorrowerMember().getId());
    }
}

