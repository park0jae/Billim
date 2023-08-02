package dblab.sharing_flatform.dto.trade;


import com.fasterxml.jackson.annotation.JsonInclude;
import dblab.sharing_flatform.domain.trade.Trade;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeResponseDto {

    private Long tradeId;
    private Long postId;
    private String renderMember;
    private String borrowerMember;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean tradeComplete;
    private boolean writtenReview;


    public static TradeResponseDto toDto(Trade trade) {
        if (trade != null) {
            return new TradeResponseDto(
                    trade.getId(),
                    trade.getPost().getId(),
                    trade.getRenderMember().getNickname(),
                    trade.getBorrowerMember().getNickname(),
                    trade.getStartDate(),
                    trade.getEndDate(),
                    trade.isTradeComplete(),
                    trade.isWrittenReview()
                    );

        } else {
            return null;
        }
    }
}
