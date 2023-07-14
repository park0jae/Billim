package dblab.sharing_flatform.dto.review.crud.create;

import dblab.sharing_flatform.domain.item.Item;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.dto.item.crud.create.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeRequestDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeResponseDto;
import lombok.*;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    private String content;
    private double starRating;

}
