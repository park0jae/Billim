package dblab.sharing_flatform.service.notification;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.notification.Notification;
import dblab.sharing_flatform.domain.notification.NotificationType;
import dblab.sharing_flatform.dto.notification.crud.create.NotificationRequestDto;
import dblab.sharing_flatform.dto.notification.crud.create.NotificationResponseDto;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.emitter.EmitterRepositoryImpl;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 100 * 60L;
    private final EmitterRepositoryImpl emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SseEmitter subscribe(Long memberId, String lastEventId){
        // 현재 회원의 id와 lastEvent ID로 고유의 emitter를 생성함.
        String emitterId = makeTimeIncludeId(memberId); // emitterId = memberId_currentTime
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(memberId);

        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }
        return emitter;
    }

    @Transactional
    public void unSubscribe(Long memberId){
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartsWithByMemberId(String.valueOf(memberId));
        Iterator<Map.Entry<String, SseEmitter>> iterator = emitters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SseEmitter> entry = iterator.next();
            if (entry.getKey().startsWith(String.valueOf(memberId))){
                entry.getValue().complete();
                iterator.remove();
            }
        }
    }


    public void send(NotificationRequestDto requestDto){
        Member receiver = memberRepository.findByUsername(requestDto.getReceiver()).orElseThrow(MemberNotFoundException::new);

        // 알림 생성
        Notification notification = notificationRepository.save(
                new Notification(
                        requestDto.getContent(),
                        NotificationType.valueOf(requestDto.getNotificationType()),
                        receiver));

        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();

        // receiver의 에미터에 이벤트 캐시를 담아 알림 전송
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartsWithByMemberId(receiverId);
        emitters.forEach((key,emitter) -> {
            emitterRepository.saveEventCache(key, notification);
            sendNotification(emitter, eventId, key, NotificationResponseDto.toDto(notification));
        });
    }


    // Dummy 데이터 전송 함수
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try{
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        }catch (IOException exception){
            emitterRepository.deleteById(emitterId);
        }
    }


    private boolean hasLostData(String lastEventId){
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter){
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartsWithByMemberId(String.valueOf(memberId));

        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0 )
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    private static String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }


}
