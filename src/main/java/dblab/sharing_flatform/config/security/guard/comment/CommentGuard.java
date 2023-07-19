package dblab.sharing_flatform.config.security.guard.comment;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.domain.comment.Comment;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.repository.comment.CommentRepository;
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
