package dblab.sharing_platform.config.security.guard.review;

import dblab.sharing_platform.config.security.guard.Guard;
import dblab.sharing_platform.config.security.util.SecurityUtil;
import dblab.sharing_platform.domain.trade.Trade;
import dblab.sharing_platform.exception.guard.GuardException;
import dblab.sharing_platform.repository.trade.TradeRepository;
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

