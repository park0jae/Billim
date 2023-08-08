package dblab.sharing_platform.dto.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedPostListDto {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<PostDto> postList;

    public static PagedPostListDto toDto(Page<PostDto> page) {
        return new PagedPostListDto(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent()
        );
    }
}
