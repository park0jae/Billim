package dblab.sharing_platform.repository.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String eventCacheId, Object event);
    Map<String, SseEmitter> findAllEmitterStartsWithByMemberId(String memberId);
    Map<String, Object> findAllEventCacheStartsWithByMemberId(String memberId);
    void deleteById(String id);
    void deleteAllEmitterStartsWithId(String memberId);
    void deleteAllEventCacheStartsWithId(String memberId);
}
