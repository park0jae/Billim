package dblab.sharing_platform.domain.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dblab.sharing_platform.factory.message.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    Message message;

    @BeforeEach
    void beforeEach() {
        message = createMessage();
    }

    @Test
    @DisplayName("송신자에 의한 메시지 삭제")
    void deleteBySenderTest() {
        // when
        message.deleteBySender();

        // then
        assertThat(message.isDeleteBySender()).isTrue();
    }

    @Test
    @DisplayName("수신자에 의한 메시지 삭제")
    void deleteByReceiverTest() {
        // when
        message.deleteByReceiver();

        // then
        assertThat(message.isDeleteByReceiver()).isTrue();
    }

    @Test
    @DisplayName("메시지 최초 생성 시 삭제 가능 여부")
    public void isNotDeletableTest() throws Exception {
        // then
        assertThat(message.isDeletable()).isFalse();
    }

    @Test
    @DisplayName("수신자와 송신자에 의해 메시지가 삭제되었을 때 실제 삭제 수행")
    void isDeletableTest() {
        // given
        message.deleteBySender();
        message.deleteByReceiver();

        // when
        boolean deletable = message.isDeletable();

        // then
        assertThat(deletable).isTrue();
    }
}