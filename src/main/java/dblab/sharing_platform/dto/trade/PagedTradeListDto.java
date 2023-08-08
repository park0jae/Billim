package dblab.sharing_platform.dto.trade;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedTradeListDto {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<TradeDto> tradeList;

    public static PagedTradeListDto toDto(Page<TradeDto> page) {
        return new PagedTradeListDto(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent()
        );
    }
}
