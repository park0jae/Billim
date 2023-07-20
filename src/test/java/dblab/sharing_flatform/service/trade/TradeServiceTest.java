package dblab.sharing_flatform.service.trade;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.trade.crud.create.TradeRequestDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeResponseDto;
import dblab.sharing_flatform.factory.trade.TradeFactory;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @InjectMocks
    private TradeService tradeService;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    Trade trade;
    Post post;
    Member renderMember;
    Member borrowerMember;

    @BeforeEach
    public void beforeEach(){
        trade = TradeFactory.createTrade();
        renderMember = trade.getRenderMember();
        borrowerMember = trade.getBorrowerMember();
        post = trade.getPost();
    }

    @Test
    public void createTradeTest(){
        // Given
        TradeRequestDto tradeRequestDto = new TradeRequestDto(renderMember.getUsername(), borrowerMember.getUsername(), LocalDate.now(), LocalDate.now());

        given(memberRepository.findByUsername(tradeRequestDto.getBorrowerName())).willReturn(Optional.of(borrowerMember));
        given(memberRepository.findByUsername(tradeRequestDto.getRenderName())).willReturn(Optional.of(renderMember));

        given(postRepository.findById(trade.getPost().getId())).willReturn(Optional.of(post));

        // When
        TradeResponseDto result = tradeService.createTrade(tradeRequestDto, post.getId());

        // Then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void completeTradeTest(){
        // Given
        given(tradeRepository.findById(trade.getId())).willReturn(Optional.of(trade));

        // When
        tradeService.completeTrade(trade.getId());

        // Then
        Assertions.assertThat(trade.isTradeComplete()).isTrue();
    }
}
