package dblab.sharing_flatform.config.security.guard.reply;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.domain.reply.Reply;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyGuard extends Guard {

    private final ReplyRepository replyRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Reply reply = replyRepository.findById(id).orElseThrow(AccessDeniedException::new);
        return reply.getId().equals(id);
    }
}
