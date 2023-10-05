package dblab.sharing_platform.config.security.guard.message;

import dblab.sharing_platform.config.security.guard.Guard;
import dblab.sharing_platform.domain.message.Message;
import dblab.sharing_platform.exception.guard.GuardException;
import dblab.sharing_platform.repository.message.MessageRepository;
import dblab.sharing_platform.config.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiveMessageGuard extends Guard {

    private final MessageRepository messageRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(GuardException::new);
        Long receiverId = message.getReceiveMember().getId();

        Long memberId = Long.valueOf(SecurityUtil.getCurrentUserId().get());

        return memberId.equals(receiverId);
    }
}
