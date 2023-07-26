package dblab.sharing_flatform.dto.report;

import dblab.sharing_flatform.domain.embedded.report_type.ReportType;
import dblab.sharing_flatform.domain.report.Report;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ReportDto {

    private String reporter;

    private ReportType reportType;

    private String content;

    private Long postId;
    private String reportedName;



    public static ReportDto toDto(Report report) {
         return new ReportDto(report.getReporter().getNickname(), report.getReportType(), report.getContent(), report.getPost().getId(), report.getReported().getNickname());
     }

    public static List<ReportDto> toDtoList(List<Report> reports) {
        return reports.stream().map(r -> ReportDto.toDto(r)).collect(Collectors.toList());
    }
}
