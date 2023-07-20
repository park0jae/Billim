package dblab.sharing_flatform.domain.trade;


import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.factory.post.PostFactory;
import dblab.sharing_flatform.factory.trade.TradeFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static dblab.sharing_flatform.factory.post.PostFactory.*;
import static dblab.sharing_flatform.factory.trade.TradeFactory.*;

public class TradeTest {

    @Test
    public void createTradeTest(){

        // given
        Trade trade = createTrade();

        // then
        Assertions.assertThat(trade.getRenderMember().getUsername()).isEqualTo("빌려주는사람");
    }
}
