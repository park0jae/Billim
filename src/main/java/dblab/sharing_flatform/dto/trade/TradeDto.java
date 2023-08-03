package dblab.sharing_flatform.dto.trade;

import dblab.sharing_flatform.domain.trade.Trade;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeDto {

    private Long tradeId;
    private Long postId;
    private String renderMember;
    private String borrowerMember;

    public static TradeDto toDto(Trade trade) {
        return new TradeDto(trade.getId(),
                trade.getPost().getId(),
                trade.getRenderMember().getNickname(),
                trade.getBorrowerMember().getNickname());
    }
    public TradeDto(Long tradeId, Long postId, String renderMember, String borrowerMember) {
        this.tradeId = tradeId;
        this.postId = postId;
        this.renderMember = renderMember;
        this.borrowerMember = borrowerMember;
    }
}
