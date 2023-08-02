package dblab.sharing_flatform.repository.member;

import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.member.MemberPagingCondition;
import org.springframework.data.domain.Page;

public interface QMemberRepository {

    // 회원 검색 및 페이징 ( Querydsl 사용 )
    // 검색조건 : username -> paging
    Page<MemberDto> findAllBySearch(MemberPagingCondition cond);
}
