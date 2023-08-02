package dblab.sharing_flatform.dto.report;

import com.querydsl.core.annotations.QueryProjection;
import dblab.sharing_flatform.domain.embedded.report_type.ReportType;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

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
}
