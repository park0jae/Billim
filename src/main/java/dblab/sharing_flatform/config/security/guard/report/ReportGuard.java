package dblab.sharing_flatform.config.security.guard.report;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportGuard extends Guard {
    private final ReportRepository reportRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(GuardException::new);

        return Long.valueOf(SecurityUtil.getCurrentUserId().get()).equals(report.getReporter().getId());
    }
}
