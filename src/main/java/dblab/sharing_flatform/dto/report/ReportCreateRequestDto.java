package dblab.sharing_flatform.dto.report;

import dblab.sharing_flatform.domain.embedded.report_type.ReportType;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.report.Report;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportCreateRequestDto {

    @NotNull
    private ReportType reportType;
    
    private Long postId;

    @NotEmpty(message = "신고 내용을 입력해주세요.")
    private String content;

}
