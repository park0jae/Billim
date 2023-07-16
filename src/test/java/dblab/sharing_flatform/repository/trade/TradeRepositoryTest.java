package dblab.sharing_flatform.repository.trade;

import dblab.sharing_flatform.config.querydsl.QuerydslConfig;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.factory.post.PostFactory;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

import static dblab.sharing_flatform.factory.category.CategoryFactory.createCategory;
import static dblab.sharing_flatform.factory.item.ItemFactory.createItem;
import static dblab.sharing_flatform.factory.member.MemberFactory.createBorrowerMember;
import static dblab.sharing_flatform.factory.member.MemberFactory.createRenderMember;
import static dblab.sharing_flatform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
public class TradeRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Post post;
    Trade trade;
    Member member;
    Member borrowerMember;
    Category category;

    List<Trade> trades = List.of();

    @BeforeEach
    public void beforeEach(){
        postRepository.deleteAll();
        tradeRepository.deleteAll();
        memberRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    // 멤버에 대한 글 하나가 만들어 진거임.
    public void tradeInit(){
        // 대여자
        member = createRenderMember();
        memberRepository.save(member);
        clear();

        // 대출자
        borrowerMember = createBorrowerMember();
        memberRepository.save(borrowerMember);
        clear();

        // 카테고리
        category = createCategory();
        categoryRepository.save(category);
        clear();

        // 글
        post = createPost(category, member);
        postRepository.save(post);
        clear();

        // 거래 생성
        trade = new Trade(member, borrowerMember, LocalDate.now(), LocalDate.now(), post);
        tradeRepository.save(trade);
        clear();
    }

    @Test
    public void tradeSaveTest(){
        // given
        tradeInit();

        // then
        assertThat(tradeRepository.count()).isEqualTo(1);
        assertThat(postRepository.findById(post.getId()).get().getTrade().getId()).isEqualTo(trade.getId());

    }

    @Test
    public void deleteCascadePost() {
        //given
        tradeInit();

        // when
        postRepository.delete(post);
        clear();

        // then
        assertThat(tradeRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void deleteCascadeMember(){
        //given
        tradeInit();

        // when
        memberRepository.delete(member);
        clear();

        // then
        assertThat(tradeRepository.count()).isEqualTo(0);
    }

    @Test
    public void findTradeByPostId(){
        //given
        tradeInit();

        // when
        Trade findTrade = tradeRepository.findByPostId(post.getId()).orElseThrow(TradeNotFoundException::new);

        // then
        assertThat(findTrade.getPost().getId()).isEqualTo(post.getId());
    }

    public void clear(){
        em.flush();
        em.clear();
    }

}
