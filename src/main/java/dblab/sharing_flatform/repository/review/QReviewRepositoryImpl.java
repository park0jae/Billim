package dblab.sharing_flatform.repository.review;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.dto.review.ReviewDto;
import dblab.sharing_flatform.dto.review.crud.read.request.ReviewPagingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static dblab.sharing_flatform.domain.review.QReview.review;

@Repository
public class QReviewRepositoryImpl extends QuerydslRepositorySupport implements QReviewRepository {
    private final JPAQueryFactory query;

    public QReviewRepositoryImpl(JPAQueryFactory query) {
        super(Review.class);
        this.query = query;
    }

    @Override
    public Page<ReviewDto> findAllByUsername(ReviewPagingCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateByNickname(cond); // 검색 조건
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    @Override
    public Page<ReviewDto> findAllWithMemberByCurrentUsername(ReviewPagingCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateByCurrentUsername(cond); // 검색 조건
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    @Override
    public Page<ReviewDto> findAllReviews(ReviewPagingCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateAll(); // 검색 조건
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private Predicate createPredicateByNickname(ReviewPagingCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(cond.getNickname())) {
            builder.and(review.writer.nickname.eq(cond.getNickname()));
        }
        return builder;
    }

    private Predicate createPredicateByCurrentUsername(ReviewPagingCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(cond.getUsername())) {
            builder.and(review.member.username.eq(cond.getUsername()));
        }
        return builder;
    }

    private Predicate createPredicateAll() {
        BooleanBuilder builder = new BooleanBuilder();
        return builder;
    }

    private List<ReviewDto> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query
                        .select(constructor(ReviewDto.class,
                                review.member.nickname,
                                review.writer.nickname,
                                review.content))
                        .from(review)
                        .join(review.writer)
                        .where(predicate)
                        .orderBy(review.id.asc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return query.select(review.count()).from(review).where(predicate).fetchOne();
    }
}
