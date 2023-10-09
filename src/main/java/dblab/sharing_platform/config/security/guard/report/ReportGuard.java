package dblab.sharing_platform.config.security.guard.report;

import dblab.sharing_platform.config.security.guard.Guard;
import dblab.sharing_platform.config.security.util.SecurityUtil;
import dblab.sharing_platform.domain.report.Report;
import dblab.sharing_platform.exception.guard.GuardException;
import dblab.sharing_platform.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportGuard extends Guard {
    private final ReportRepository reportRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(GuardException::new);

        return Long.valueOf(SecurityUtil.getCurrentUserId().get()).equals(report.getReporter().getId());
    }
}
