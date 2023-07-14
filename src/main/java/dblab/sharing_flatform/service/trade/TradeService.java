package dblab.sharing_flatform.service.trade;


import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.post.crud.read.response.PagedPostListDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeRequestDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeResponseDto;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.trade.ExistTradeException;
import dblab.sharing_flatform.exception.trade.ImpossibleCreateTradeException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public TradeResponseDto createTrade(TradeRequestDto tradeRequestDto, Long id){

        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        String borrowerUsername = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        Member member = memberRepository.findByUsername(post.getMember().getUsername()).orElseThrow(MemberNotFoundException::new);

        if(member.getUsername().equals(borrowerUsername))
            throw new ImpossibleCreateTradeException();
        if(tradeRepository.findByPostId(id).isPresent()){
            throw new ExistTradeException();
        }

        Trade trade = new Trade(member,
                memberRepository.findByUsername(borrowerUsername).orElseThrow(MemberNotFoundException::new),
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

        tradeRepository.delete(trade);
    }

    @Transactional
    public void cancelByBorrower(Long id){
        Trade trade = tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new);

        tradeRepository.delete(trade);
    }



}
