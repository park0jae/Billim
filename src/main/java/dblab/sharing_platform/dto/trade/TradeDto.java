package dblab.sharing_platform.dto.trade;

import dblab.sharing_platform.domain.trade.Trade;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeDto {

    private Long tradeId;
    private Long postId;
    private String postTitle;
    private String renderMember;
    private String borrowerMember;

    public static TradeDto toDto(Trade trade) {
        return new TradeDto(trade.getId(),
                trade.getPost().getId(),
                trade.getPost().getTitle(),
                trade.getRenderMember().getNickname(),
                trade.getBorrowerMember().getNickname());
    }
    public TradeDto(Long tradeId, Long postId, String postTitle, String renderMember, String borrowerMember) {
        this.tradeId = tradeId;
        this.postId = postId;
        this.postTitle = postTitle;
        this.renderMember = renderMember;
        this.borrowerMember = borrowerMember;
    }
}
