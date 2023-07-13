package dblab.sharing_flatform.service.trade;


import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.post.crud.read.response.PagedPostListDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeRequestDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeResponseDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 1. 글을 올린사람이 거래를 생성함
    // 거래 (사용 기간)

    @Transactional
    public TradeResponseDto createTrade(TradeRequestDto tradeRequestDto){

        Post post = postRepository.findById(tradeRequestDto.getPostId()).orElseThrow(PostNotFoundException::new);

        // 글 올린 사람을 빌려주는 사람으로 지정
        Trade trade = new Trade(memberRepository.findByUsername(post.getMember().getUsername()).orElseThrow(MemberNotFoundException::new),
                memberRepository.findByUsername(tradeRequestDto.getBorrowerMember()).orElseThrow(MemberNotFoundException::new),
                tradeRequestDto.getStartDate(),
                tradeRequestDto.getEndDate()
        );

        post.addTrade(trade);
        tradeRepository.save(trade);

        return TradeResponseDto.toDto(trade);
    }

    public TradeResponseDto findTradeById(Long id){
        return TradeResponseDto.toDto(tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new));
    }

    public List<TradeResponseDto> findAllTrade() {
        return tradeRepository.findAll().stream().map(trade -> TradeResponseDto.toDto(trade)).collect(Collectors.toList());
    }

    @Transactional
    public void cancelByRender(Long id){
        Trade trade = tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new);
        trade.cancelByRender();

        if(trade.isCancelable()) tradeRepository.delete(trade);
    }

    @Transactional
    public void cancelByBorrower(Long id){
        Trade trade = tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new);
        trade.cancelByBorrower();

        if(trade.isCancelable()) tradeRepository.delete(trade);
    }



}
