package dblab.sharing_platform.dto.report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.List;
import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
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
