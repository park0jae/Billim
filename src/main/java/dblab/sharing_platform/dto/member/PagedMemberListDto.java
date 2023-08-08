package dblab.sharing_platform.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedMemberListDto {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<MemberDto> memberList;

    public static PagedMemberListDto toDto(Page<MemberDto> page) {
        return new PagedMemberListDto(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent()
        );
    }
}
