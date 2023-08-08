package dblab.sharing_platform.config.security.guard.post;

import dblab.sharing_platform.config.security.guard.Guard;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.exception.guard.GuardException;
import dblab.sharing_platform.repository.post.PostRepository;
import dblab.sharing_platform.config.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostGuard extends Guard {

    private final PostRepository postRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Post post = postRepository.findById(id).orElseThrow(GuardException::new);
        Long memberId = Long.valueOf(SecurityUtil.getCurrentUserId().orElseThrow(GuardException::new));

        return post.getMember().getId().equals(memberId);
    }
}
