package dblab.sharing_flatform.service.trade;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.dto.trade.*;
import dblab.sharing_flatform.exception.trade.ExistTradeException;
import dblab.sharing_flatform.exception.trade.ImpossibleCreateTradeException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dblab.sharing_flatform.factory.trade.TradeFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


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
        trade = createTrade();
        renderMember = trade.getRenderMember();
        borrowerMember = trade.getBorrowerMember();
        post = trade.getPost();
    }

    @Test
    @DisplayName("거래 생성 테스트")
    public void createTradeTest(){
        // Given
        TradeRequestDto tradeRequestDto = new TradeRequestDto(borrowerMember.getNickname(), LocalDate.now(), LocalDate.now());

        given(memberRepository.findByUsername(renderMember.getUsername())).willReturn(Optional.of(renderMember));
        given(memberRepository.findByNickname(tradeRequestDto.getBorrowerName())).willReturn(Optional.of(borrowerMember));

        given(postRepository.findById(trade.getPost().getId())).willReturn(Optional.of(post));

        // When
        TradeResponseDto result = tradeService.createTrade(tradeRequestDto, post.getId(), renderMember.getUsername());

        // Then
        assertThat(result.getRenderMember()).isEqualTo("render");
    }

    @Test
    @DisplayName("거래 완료 테스트")
    public void completeTradeTest(){
        // Given
        given(tradeRepository.findById(trade.getId())).willReturn(Optional.of(trade));

        // When
        tradeService.completeTrade(trade.getId());

        // Then
        assertThat(trade.isTradeComplete()).isTrue();
    }

    @Test
    @DisplayName("거래 아이디로 거래 내역 조회 테스트")
    public void findTradeById(){
        // Given
        given(tradeRepository.findById(trade.getId())).willReturn(Optional.of(trade));

        // When
        TradeResponseDto result = tradeService.findTradeById(trade.getId());

        // Then
        assertThat(result.getPostId()).isEqualTo(trade.getId());
    }

    @Test
    @DisplayName("전체 거래 내역 조회 테스트")
    public void findAllTrade() {
        // Given
        List<Trade> tradeList = new ArrayList<>();
        tradeList.add(createTrade());
        tradeList.add(createTradeReverse());

        TradePagingCondition cond = new TradePagingCondition(0, 10, null, null, null, null, null);
        given(tradeRepository.findAllByCond(cond)).willReturn(
                new PageImpl<>(List.of(TradeDto.toDto(tradeList.get(0)),
                        TradeDto.toDto(tradeList.get(1))), PageRequest.of(cond.getPage(), cond.getSize()), 1));

        // When
        PagedTradeListDto result = tradeService.findAllByCond(cond);

        // Then
        assertThat(result.getTradeList().size()).isEqualTo(2);

        TradeDto tradeDto0 = result.getTradeList().get(0);
        assertThat(tradeDto0.getRenderMember()).isEqualTo("render");
        assertThat(tradeDto0.getBorrowerMember()).isEqualTo("borrower");

        TradeDto tradeDto = result.getTradeList().get(1);
        assertThat(tradeDto.getRenderMember()).isEqualTo("borrower");
        assertThat(tradeDto.getBorrowerMember()).isEqualTo("render");
    }
    @Test
    @DisplayName("거래 삭제 테스트")
    public void deleteTradeTest(){
        // Given
        given(tradeRepository.findById(trade.getId())).willReturn(Optional.of(trade));

        // When
        tradeService.deleteTrade(trade.getId());

        // Then
        verify(tradeRepository).delete(trade);
    }
    @Test
    @DisplayName("거래 내역이 존재 X (예외 처리 테스트)")
    public void TradeNotFoundExceptionTest(){
        // Given
        Long tradeId = 100L;

        given(tradeRepository.findById(tradeId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> tradeService.findTradeById(tradeId)).isInstanceOf(TradeNotFoundException.class);
    }

    @Test
    @DisplayName("거래 중복 생성 예외 테스트")
    public void existTradeExceptionTest(){
        // Given
        TradeRequestDto tradeRequestDto = new TradeRequestDto(trade.getBorrowerMember().getNickname(), LocalDate.now(), LocalDate.now());

        given(memberRepository.findByUsername(trade.getRenderMember().getUsername())).willReturn(Optional.of(renderMember));
        given(memberRepository.findByNickname(trade.getBorrowerMember().getNickname())).willReturn(Optional.of(borrowerMember));
        given(tradeRepository.findByPostId(post.getId())).willReturn(Optional.of(trade));

        // When & Then
        assertThatThrownBy(() -> tradeService.createTrade(tradeRequestDto, trade.getPost().getId(), renderMember.getUsername())).isInstanceOf(ExistTradeException.class);
    }

    @Test
    @DisplayName("거래 생성 불가 예외 테스트")
    public void impossibleCreateTradeExceptionTest(){
        // Given
        trade = createTradeEqualMember();
        TradeRequestDto tradeRequestDto = new TradeRequestDto(trade.getBorrowerMember().getNickname(), LocalDate.now(), LocalDate.now());

        given(memberRepository.findByUsername(trade.getRenderMember().getUsername())).willReturn(Optional.of(renderMember));
        given(memberRepository.findByNickname(trade.getBorrowerMember().getNickname())).willReturn(Optional.of(renderMember));

        // When & Then
        assertThatThrownBy(() -> tradeService.createTrade(tradeRequestDto, trade.getPost().getId(), renderMember.getUsername())).isInstanceOf(ImpossibleCreateTradeException.class);
    }
}
