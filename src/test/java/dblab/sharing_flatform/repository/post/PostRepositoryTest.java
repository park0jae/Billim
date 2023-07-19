package dblab.sharing_flatform.repository.post;

import dblab.sharing_flatform.config.querydsl.QuerydslConfig;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.image.PostImage;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.image.PostImageRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

import static dblab.sharing_flatform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class PostRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostImageRepository postImageRepository;

    @Autowired
    MemberRepository memberRepository;

    Post post;
    Member member;
    Category category;

    @BeforeEach
    public void initDB() {
        memberRepository.deleteAll();
        categoryRepository.deleteAll();
        postImageRepository.deleteAll();
        postRepository.deleteAll();
    }

    public void postInit() {
        post = createPost();
        category = post.getCategory();
        member = post.getMember();

        memberRepository.save(member);
        clear();

        categoryRepository.save(category);
        clear();

        postRepository.save(post);
        clear();
    }

    public void postInitWithImages(List<PostImage> postImages) {
        post = createPost(postImages);
        category = post.getCategory();
        member = post.getMember();

        memberRepository.save(member);
        clear();

        categoryRepository.save(category);
        clear();

        postRepository.save(post);
        clear();
    }

    @Test
    public void deleteCascadeCategory() throws Exception {
        // given
        postInit();

        // when
        categoryRepository.delete(category);
        clear();

        // then
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }


    @Test
    public void deleteCascadeMember() throws Exception {
        //given
        postInit();

        //when
        memberRepository.delete(member);
        clear();

        //then
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }

    @Test
    public void persistImageCascadePost() throws Exception {
        //given
        List<PostImage> postImages = new ArrayList<>();
        postImages.add(new PostImage("testImage1.jpg"));
        postImages.add(new PostImage("testImage2.jpg"));

        //when
        postInitWithImages(postImages);

        //then
        assertThat(postImageRepository.findAll().size()).isEqualTo(2);
    }

    @Test  
    public void findAllBySearchTest() throws Exception {
        //given
        postInit();

        PostPagingCondition cond = new PostPagingCondition(0, 10, "category1", "title");

        //when
        Page<PostDto> result = postRepository.findAllByCategoryAndTitle(cond);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
    }


    public void clear() {
        em.flush();
        em.clear();
    }
}