package dblab.sharing_platform.repository.report;

import dblab.sharing_platform.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, QReportRepository {

}
