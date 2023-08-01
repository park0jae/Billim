package dblab.sharing_flatform.domain.report;

import dblab.sharing_flatform.domain.embedded.report_type.ReportType;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ReportType reportType;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reporter_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reporter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reported_id", nullable = true)
    private Member reported;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    public Report(ReportType reportType, String content, Member reporter, Post post, Member reported) {
        this.reportType = reportType;
        this.content = content;
        this.reporter = reporter;
        this.post = post;
        this.reported = reported;
    }
}
