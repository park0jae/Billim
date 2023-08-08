package dblab.sharing_platform.dto.report;

import dblab.sharing_platform.domain.embedded.report_type.ReportType;
import dblab.sharing_platform.domain.report.Report;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportDto {

    private Long reportId;
    private String reporter_nickname;
    private ReportType reportType;
    private String content;

    private Long postId;
    private String reported_nickname;

    public ReportDto(Long reportId, String reporter_nickname, ReportType reportType, String content, Long postId, Report report) {
        this.reportId = reportId;
        this.reporter_nickname = reporter_nickname;
        this.reportType = reportType;
        this.content = content;
        this.postId = postId;
        this.reported_nickname = (report.getReported() == null ? null : report.getReported().getNickname());
    }

    public static ReportDto toDto(Report report) {
        if (report.getPost() != null) {
            return new ReportDto(report.getId(), report.getContent(), report.getReportType(), report.getContent(), report.getPost().getId(), report.getReporter().getNickname());
        }
        return new ReportDto(report.getId(), report.getContent(), report.getReportType(), report.getContent(), null, report.getReporter().getNickname());
    }
}
