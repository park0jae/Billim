package dblab.sharing_flatform.repository.report;

import dblab.sharing_flatform.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
