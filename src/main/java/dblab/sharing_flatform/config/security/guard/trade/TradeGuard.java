package dblab.sharing_flatform.config.security.guard.trade;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeGuard extends Guard {

    private final TradeRepository tradeRepository;
    @Override
    protected boolean isResourceOwner(Long id) {
        Long currentId = Long.valueOf(SecurityUtil.getCurrentUserId().orElseThrow(GuardException::new)); // 현재 아이디

        Member render = tradeRepository.findById(id).orElseThrow(GuardException::new).getRenderMember();
        Member borrower = tradeRepository.findById(id).orElseThrow(GuardException::new).getBorrowerMember();

        return currentId.equals(render.getId()) || currentId.equals(borrower.getId());
    }
}
