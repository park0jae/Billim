package dblab.sharing_flatform.config.security.guard.message;

import dblab.sharing_flatform.config.security.guard.Guard;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.repository.message.MessageRepository;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendMessageGuard extends Guard {

    private final MessageRepository messageRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(GuardException::new);
        Long senderId = message.getSendMember().getId();

        Long memberId = Long.valueOf(SecurityUtil.getCurrentUserId().get());


        return memberId.equals(senderId);
    }
}
