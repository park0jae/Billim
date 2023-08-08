package dblab.sharing_platform.repository.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter); // Emitter 저장
    void saveEventCache(String eventCacheId, Object event); // 이벤트 저장
    Map<String, SseEmitter> findAllEmitterStartsWithByMemberId(String memberId); // 해당 관련 모든 Emitter 조회
    Map<String, Object> findAllEventCacheStartsWithByMemberId(String memberId); // 해당 회원과 관련된 모든 이벤트 조회
    void deleteById(String id); // Emitter 삭제
    void deleteAllEmitterStartsWithId(String memberId); // 해당 회원과 관련된 모든 Emitter 지움
    void deleteAllEventCacheStartsWithId(String memberId); // 해당 회원과 관련된 모든 이벤트 지움
}
