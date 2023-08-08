package dblab.sharing_platform.factory.trade;

import dblab.sharing_platform.domain.trade.Trade;

import java.time.LocalDate;

import static dblab.sharing_platform.factory.member.MemberFactory.*;
import static dblab.sharing_platform.factory.post.PostFactory.*;

public class TradeFactory {

    public static Trade createTrade(){
        return new Trade(createRenderMember(), createBorrowerMember(), LocalDate.now(), LocalDate.now(), createPost());
    }

    public static Trade createTradeReverse(){
        return new Trade(createBorrowerMember(), createRenderMember(), LocalDate.now(), LocalDate.now(), createPost());
    }

    public static Trade createTradeEqualMember(){
        return new Trade(createRenderMember(), createRenderMember(), LocalDate.now(), LocalDate.now(), createPost());
    }

}
