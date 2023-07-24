package dblab.sharing_flatform.service.notification;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.notification.Notification;
import dblab.sharing_flatform.domain.notification.NotificationType;
import dblab.sharing_flatform.dto.notification.crud.create.NotificationRequestDto;
import dblab.sharing_flatform.dto.notification.crud.create.NotificationResponseDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.emitter.EmitterRepositoryImpl;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 100 * 60;
    private final EmitterRepositoryImpl emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;


    public SseEmitter subscribe(Long memberId, String lastEventId){
        // 현재 회원의 id와 lastEvent ID로 고유의 emitter를 생성함.
        String emitterId = makeTimeIncludeId(memberId); // emitterId = memberId_currentTime
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // 시간이 만료된 경우 자동으로 리포지토리에서 삭제 처리해주는 콜백 등록
        // 만료된 Emitter는 Repo에서 자동 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // event_Id = memberId_currentTime
        String eventId = makeTimeIncludeId(memberId);

        // 처음 연결한 경우 더미데이터 전송해서 생성한 emitter 에 event_ID와 data를 전송
        // SseEmitter의 유효 시간동안 어느 데이터도 전송되지 않는다면 503 에러를 발생시킴
        // 이에 대한 방안으로 맨 처음 연결을 진행한다면 Dummy 데이터를 보내 이를 방지
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        // Last-Event-ID가 존재한다는 것은 받지 못한 데이터가 있다는 것
        if (hasLostData(lastEventId)) {
            // Last-Event-ID를 기준으로 그 뒤의 데이터를 추출해 알림을 보내주기.
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }
        return emitter;
    }

    public void send(NotificationRequestDto requestDto){
        log.info("DID");
        Member receiver = memberRepository.findByUsername(requestDto.getReceiver()).orElseThrow(MemberNotFoundException::new);
        log.info("DID2");

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
