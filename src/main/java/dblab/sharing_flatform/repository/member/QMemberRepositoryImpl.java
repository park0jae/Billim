package dblab.sharing_flatform.repository.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.member.crud.read.request.MemberPagingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static dblab.sharing_flatform.domain.member.QMember.member;

@Repository
public class QMemberRepositoryImpl extends QuerydslRepositorySupport implements QMemberRepository {
    private final JPAQueryFactory query;

    public QMemberRepositoryImpl(JPAQueryFactory query) {
        super(Member.class);
        this.query = query;
    }

    @Override
    public Page<MemberDto> findAllBySearch(MemberPagingCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond); // 검색 조건

        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private Predicate createPredicate(MemberPagingCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(cond.getUsername())) {
            builder.and(member.username.containsIgnoreCase(cond.getUsername()));
        }

        return builder;
    }

    private List<MemberDto> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query
                        .select(
                                constructor(MemberDto.class,
                                            member.id,
                                            member.username,
                                            member))
                        .from(member)
                        .where(predicate)
                        .orderBy(member.id.asc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return query.select(member.count()).from(member).where(predicate).fetchOne();
    }
}
