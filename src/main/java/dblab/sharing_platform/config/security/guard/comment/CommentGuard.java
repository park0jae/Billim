package dblab.sharing_platform.config.security.guard.comment;

import dblab.sharing_platform.config.security.guard.Guard;
import dblab.sharing_platform.domain.comment.Comment;
import dblab.sharing_platform.exception.guard.GuardException;
import dblab.sharing_platform.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentGuard extends Guard {
    private final CommentRepository commentRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(GuardException::new);
        return comment.getId().equals(id);
    }
}
