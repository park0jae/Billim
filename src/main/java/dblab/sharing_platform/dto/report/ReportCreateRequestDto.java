package dblab.sharing_platform.dto.report;

import dblab.sharing_platform.domain.embedded.report_type.ReportType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
