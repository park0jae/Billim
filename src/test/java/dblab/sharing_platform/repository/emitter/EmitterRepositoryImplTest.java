package dblab.sharing_platform.repository.emitter;

import dblab.sharing_platform.config.querydsl.QuerydslConfig;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.notification.Notification;
import dblab.sharing_platform.domain.notification.NotificationType;
import dblab.sharing_platform.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Map;

import static dblab.sharing_platform.factory.member.MemberFactory.createMember;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class EmitterRepositoryImplTest {

    private EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    @DisplayName("emitter 생성 테스트")
    public void createEmitterTest() throws Exception {
        //given
        Long memberId = 1L;
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        //then
        Assertions.assertDoesNotThrow(() -> emitterRepository.save(emitterId, sseEmitter));

    }

    @Test
    @DisplayName("수신한 이벤트를 캐시에 저장")
    public void saveEventCacheTest() throws Exception {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        String eventcacheId = member.getId() + "_" + System.currentTimeMillis();
        Notification notification = new Notification("메시지가 도착했습니다.", NotificationType.MESSAGE, member);

        //then
        assertDoesNotThrow(() -> emitterRepository.saveEventCache(eventcacheId, notification));
    }

    @Test
    @DisplayName("한 회원이 생성한 모든 Emitter 조회")
    public void findAllEmitterStartsWithByMemberIdTest() throws Exception {
        //given
        Long memberId = 1L;
        String emitterId = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        sleep(100);
        String emitterId2 = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId2, new SseEmitter(DEFAULT_TIMEOUT));

        sleep(100);
        String emitterId3 = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId3, new SseEmitter(DEFAULT_TIMEOUT));


        //when
        Map<String, SseEmitter> result = emitterRepository.findAllEmitterStartsWithByMemberId(String.valueOf(memberId));

        //then
        assertThat(result.size()).isEqualTo(3);

    }
    
    @Test
    @DisplayName("회원이 수신한 이벤트를 캐시에서 모두 조회")
    public void findAllEventCacheStartsWithByMemberId() throws Exception {
        //given
        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();
        clear();

        String eventCacheId = memberId + "_" + System.currentTimeMillis();
        Notification notification = new Notification("이벤트", NotificationType.MESSAGE, member);
        sleep(100);
        emitterRepository.saveEventCache(eventCacheId, notification);

        sleep(100);
        String eventCacheId2 = memberId + "_" + System.currentTimeMillis();
        Notification notification2 = new Notification("이벤트", NotificationType.MESSAGE, member);
        emitterRepository.saveEventCache(eventCacheId2, notification2);


        sleep(100);
        String eventCacheId3 = memberId + "_" + System.currentTimeMillis();
        Notification notification3 = new Notification("이벤트", NotificationType.MESSAGE, member);
        emitterRepository.saveEventCache(eventCacheId3, notification3);

        //when
        Map<String, Object> result = emitterRepository.findAllEventCacheStartsWithByMemberId(String.valueOf(memberId));

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("ID를 통해 Emitter를 Repository에서 제거")
    public void deleteById() throws Exception {
        //given
        Long memberId = 1L;
        String emitterId =  memberId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        //when
        emitterRepository.save(emitterId, sseEmitter);
        emitterRepository.deleteById(emitterId);

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEmitterStartsWithByMemberId(emitterId).size());
    }

    @Test
    @DisplayName("Emitter의 수신한 이벤트 캐시 삭제")
    public void deleteAllEventCacheStartsWithId() throws Exception {
        //given
        Member member = memberRepository.save(createMember());

        String eventCacheId1 =  member.getId() + "_" + System.currentTimeMillis();
        Notification notification1 = new Notification("메시지 도착1", NotificationType.MESSAGE, member);
        emitterRepository.saveEventCache(eventCacheId1, notification1);
        sleep(100);

        String eventCacheId2 =  member.getId() + "_" + System.currentTimeMillis();
        Notification notification2 = new Notification("메시지 도착2", NotificationType.MESSAGE, member);
        emitterRepository.saveEventCache(eventCacheId2, notification2);

        //when
        emitterRepository.deleteAllEventCacheStartsWithId(String.valueOf(member.getId()));

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEventCacheStartsWithByMemberId(String.valueOf(member.getId())).size());
    }

    public void clear() {
        em.flush();
        em.clear();
    }

}