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
public class RenderTradeGuard extends Guard {

    private final TradeRepository tradeRepository;
    @Override
    protected boolean isResourceOwner(Long id) {
        // 삭제하려는 trade의 renderid와 , 현재 로그인한 id가 일치해야함
        Long currentId = Long.valueOf(SecurityUtil.getCurrentUserId().orElseThrow(AccessDeniedException::new));
        return tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new).getRenderMember().getId().equals(currentId);
    }
}
