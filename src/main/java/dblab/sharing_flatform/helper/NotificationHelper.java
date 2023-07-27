package dblab.sharing_flatform.helper;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.notification.NotificationType;
import dblab.sharing_flatform.dto.notification.crud.create.NotificationRequestDto;
import dblab.sharing_flatform.repository.emitter.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationHelper {

    private final EmitterRepository emitterRepository;
    private final ApplicationEventPublisher eventPublisher;
    public void notificationIfSubscribe(Member sendMember, Member member, NotificationType notificationType, String msg) {
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartsWithByMemberId(String.valueOf(member.getId()));
        if(!emitters.isEmpty()){
            NotificationRequestDto notificationRequestDto = new NotificationRequestDto(sendMember.getNickname() + msg, String.valueOf(notificationType), member.getNickname());
            eventPublisher.publishEvent(notificationRequestDto);
        }
    }
}
