package dblab.sharing_flatform.repository.likepost;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dblab.sharing_flatform.domain.likepost.LikePost;
import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static dblab.sharing_flatform.domain.likepost.QLikePost.likePost;

public class QLikePostRepositoryImpl extends QuerydslRepositorySupport implements QLikePostRepository {

    private final JPAQueryFactory query;

    public QLikePostRepositoryImpl(JPAQueryFactory query) {
        super(LikePost.class);
        this.query = query;
    }

    @Override
    public Page<PostDto> findAllLikesByCurrentUsername(PostPagingCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateLikes(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private Predicate createPredicateLikes(PostPagingCondition cond){
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(cond.getUsername())) {
            builder.and(likePost.member.username.eq(cond.getUsername()));
        }
        return builder;
    }

    private List<PostDto> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query
                        .select(constructor(PostDto.class,
                                likePost.id,
                                likePost.post.title,
                                likePost.post.member.username,
                                likePost.post.createdTime))
                        .from(likePost)
                        .join(likePost.member)
                        .where(predicate)
                        .orderBy(likePost.id.asc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return query.select(likePost.count()).from(likePost).where(predicate).fetchOne();
    }

}
