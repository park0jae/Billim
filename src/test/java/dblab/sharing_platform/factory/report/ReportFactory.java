package dblab.sharing_platform.factory.report;

import dblab.sharing_platform.domain.embedded.report_type.ReportType;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.domain.report.Report;

public class ReportFactory {

    public static Report createBugReport(Member reporter) {
        return new Report(ReportType.BUG,
                "content", reporter, null, null);
    }

    public static Report createPostReport(Member reporter, Post post) {
        return new Report(ReportType.POST_REPORT,
                "content", reporter, post, post.getMember());
    }
}
