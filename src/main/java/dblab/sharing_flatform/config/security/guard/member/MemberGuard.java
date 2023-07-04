package dblab.sharing_flatform.config.security.guard.member;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberGuard extends Guard {

    private final MemberRepository memberRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(AccessDeniedException::new);
        Long memberId = Long.valueOf(SecurityUtil.getCurrentUserId().get());

        return memberId.equals(id);
    }
}
