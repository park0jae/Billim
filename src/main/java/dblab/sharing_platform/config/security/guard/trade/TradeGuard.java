package dblab.sharing_platform.config.security.guard.trade;

import dblab.sharing_platform.config.security.guard.Guard;
import dblab.sharing_platform.config.security.util.SecurityUtil;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.exception.guard.GuardException;
import dblab.sharing_platform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeGuard extends Guard {

    private final TradeRepository tradeRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Long currentId = Long.valueOf(SecurityUtil.getCurrentUserId()
                .orElseThrow(GuardException::new));

        Member render = tradeRepository.findById(id)
                .orElseThrow(GuardException::new).getRenderMember();
        Member borrower = tradeRepository.findById(id)
                .orElseThrow(GuardException::new).getBorrowerMember();

        return currentId.equals(render.getId()) || currentId.equals(borrower.getId());
    }
}
