package dblab.sharing_platform.repository.member;

import dblab.sharing_platform.dto.member.MemberDto;
import dblab.sharing_platform.dto.member.MemberPagingCondition;
import org.springframework.data.domain.Page;

public interface QMemberRepository {

    // 회원 검색 및 페이징 ( Querydsl 사용 )
    // 검색조건 : username -> paging
    Page<MemberDto> findAllBySearch(MemberPagingCondition cond);
}
