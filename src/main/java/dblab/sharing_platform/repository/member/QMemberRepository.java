package dblab.sharing_platform.repository.member;

import dblab.sharing_platform.dto.member.MemberDto;
import dblab.sharing_platform.dto.member.MemberPagingCondition;
import org.springframework.data.domain.Page;

public interface QMemberRepository {

    Page<MemberDto> findAllBySearch(MemberPagingCondition cond);
}
