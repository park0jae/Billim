package dblab.sharing_flatform.service.post;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.post.crud.create.PostCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.create.PostCreateResponseDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateResponseDto;
import dblab.sharing_flatform.factory.category.CategoryFactory;
import dblab.sharing_flatform.factory.member.MemberFactory;
import dblab.sharing_flatform.factory.post.PostFactory;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.service.file.PostFileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostFileService postFileService;

    Post post;
    Member member;
    Category category;

    @BeforeEach
    public void beforeEach(){
        post = PostFactory.createPost();
        member = post.getMember();
        category = post.getCategory();
    }

    @Test
    public void createPostTest(){
        // Given
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto("테스트 타이틀", "테스트 내용",  category.getName(), List.of(), null, member.getUsername());

        given(categoryRepository.findByName(postCreateRequestDto.getCategoryName())).willReturn(Optional.of(category));
        given(memberRepository.findByUsername(postCreateRequestDto.getUsername())).willReturn(Optional.of(member));

        // when
        PostCreateResponseDto result = postService.create(postCreateRequestDto);

        assertThat(result).isNotNull();
    }

    @Test
    public void deletePostTest(){
        // Given
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        // When
        postService.delete(post.getId());

        // Then
        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    public void updatePostTest(){
        // Given
        PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto("업데이트 타이틀", "업데이트 내용", null, null, null);

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        // When
        PostUpdateResponseDto result = postService.update(post.getId(), postUpdateRequestDto);

        // Then
        assertThat(result.getTitle()).isEqualTo("업데이트 타이틀");
    }

}
