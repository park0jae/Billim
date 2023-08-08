package dblab.sharing_platform.dto.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedMessageListDto {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<MessageDto> messageList;

    public static PagedMessageListDto toDto(Page<MessageDto> page) {
        return new PagedMessageListDto(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent()
        );
    }
}