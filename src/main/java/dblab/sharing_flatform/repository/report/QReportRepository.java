package dblab.sharing_flatform.repository.report;

import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.dto.report.ReportDto;
import dblab.sharing_flatform.dto.report.ReportPagingCondition;
import org.springframework.data.domain.Page;

public interface QReportRepository {

    Page<ReportDto> findAllByCond(ReportPagingCondition cond);
}
