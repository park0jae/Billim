package dblab.sharing_platform.repository.trade;

import dblab.sharing_platform.config.querydsl.QuerydslConfig;
import dblab.sharing_platform.domain.category.Category;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.domain.trade.Trade;
import dblab.sharing_platform.exception.trade.TradeNotFoundException;
import dblab.sharing_platform.repository.category.CategoryRepository;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;

import static dblab.sharing_platform.factory.category.CategoryFactory.createCategory;
import static dblab.sharing_platform.factory.member.MemberFactory.createBorrowerMember;
import static dblab.sharing_platform.factory.member.MemberFactory.createRenderMember;
import static dblab.sharing_platform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("거래 조회")
    public void tradeSaveTest(){
        // given
        tradeInit();

        // then
        assertThat(tradeRepository.count()).isEqualTo(1);
        assertThat(postRepository.findById(post.getId()).get().getId()).isEqualTo(trade.getPost().getId());
    }

    @Test
    @DisplayName("게시글 삭제 -> 거래 삭제 by cascade")
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
    @DisplayName("회원 삭제 -> 거래 삭제 by cascade")
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
    @DisplayName("Post Id에 대한 거래 조회")
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
