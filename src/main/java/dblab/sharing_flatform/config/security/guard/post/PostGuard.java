package dblab.sharing_flatform.config.security.guard.post;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostGuard extends Guard {

    private final PostRepository postRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Post post = postRepository.findById(id).orElseThrow(AccessDeniedException::new);
        Long memberId = Long.valueOf(SecurityUtil.getCurrentUserId().orElseThrow(AccessDeniedException::new));

        return post.getMember().getId().equals(memberId);
    }
}
