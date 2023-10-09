package dblab.sharing_platform.config.security.guard.member;

import dblab.sharing_platform.config.security.guard.Guard;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.exception.guard.GuardException;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.config.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberGuard extends Guard {

    private final MemberRepository memberRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(GuardException::new);
        Long memberId = Long.valueOf(SecurityUtil.getCurrentUserId().get());

        return memberId.equals(id);
    }
}
