package dblab.sharing_platform.domain.trade;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dblab.sharing_platform.factory.trade.TradeFactory.*;

public class TradeTest {

    @Test
    @DisplayName("거래 생성")
    public void createTradeTest(){

        // given
        Trade trade = createTrade();

        // then
        Assertions.assertThat(trade.getRenderMember().getUsername()).isEqualTo("빌려주는사람");
    }
}
