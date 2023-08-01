package dblab.sharing_flatform.repository.report;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.dto.report.ReportDto;
import dblab.sharing_flatform.dto.report.ReportPagingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static dblab.sharing_flatform.domain.report.QReport.report;

public class QReportRepositoryImpl extends QuerydslRepositorySupport implements QReportRepository{

    private final JPAQueryFactory query;

    public QReportRepositoryImpl(JPAQueryFactory query) {
        super(Report.class);
        this.query = query;
    }

    @Override
    public Page<ReportDto> findAllByCond(ReportPagingCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private Predicate createPredicate(ReportPagingCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(cond.getReported())) {
            builder.and(report.reported.nickname.eq(cond.getReported()));
        }
        return builder;
    }

    private List<ReportDto> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query
                        .select(constructor(ReportDto.class,
                                report.reporter.nickname,
                                report.reportType,
                                report.content,
                                report.post.id,
                                report.reported.nickname))
                        .from(report)
                        .join(report.reported)
                        .where(predicate)
                        .orderBy(report.id.asc())
        ).fetch();
    }
    private Long fetchCount(Predicate predicate) { // 7
        return query.select(report.count()).from(report).where(predicate).fetchOne();
    }
}
