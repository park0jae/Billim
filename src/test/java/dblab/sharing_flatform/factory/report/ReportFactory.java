package dblab.sharing_flatform.factory.report;

import dblab.sharing_flatform.domain.embedded.report_type.ReportType;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.factory.member.MemberFactory;

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
