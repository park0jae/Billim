package dblab.sharing_flatform.factory.trade;

import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.factory.member.MemberFactory;
import dblab.sharing_flatform.factory.post.PostFactory;

import java.time.LocalDate;

import static dblab.sharing_flatform.factory.member.MemberFactory.*;
import static dblab.sharing_flatform.factory.post.PostFactory.*;

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
