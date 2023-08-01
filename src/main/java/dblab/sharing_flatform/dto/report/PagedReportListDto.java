package dblab.sharing_flatform.dto.report;

import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.dto.post.crud.read.response.PagedPostListDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PagedReportListDto {
    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<ReportDto> reportList;

    public static PagedReportListDto toDto(Page<ReportDto> page) {
        return new PagedReportListDto(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent()
        );
    }
}
