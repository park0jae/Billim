package dblab.sharing_flatform.service.comment;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.comment.Comment;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.comment.CommentCreateRequestDto;
import dblab.sharing_flatform.dto.comment.CommentDto;
import dblab.sharing_flatform.factory.comment.CommentFactory;
import dblab.sharing_flatform.factory.member.MemberFactory;
import dblab.sharing_flatform.factory.post.PostFactory;
import dblab.sharing_flatform.repository.comment.CommentRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static dblab.sharing_flatform.factory.comment.CommentFactory.createComment;
import static dblab.sharing_flatform.factory.member.MemberFactory.createMember;
import static dblab.sharing_flatform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private PostRepository postRepository;

    Member member;

    Post post;

    @BeforeEach
    public void beforeEach() {
        member = createMember();
        post = createPost();
    }

    @Test
    @DisplayName("댓글 생성")
    public void createCommentTest() throws Exception {
        //given
        ReflectionTestUtils.setField(post, "id", 1L);

        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        given(commentRepository.save(any())).willReturn(createComment(null));

        // when
        commentService.create(post.getId(), new CommentCreateRequestDto(member.getUsername(), "content", null));

        // then
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 전체 조회")
    public void readAllCommentOnPostTest() throws Exception {
        ReflectionTestUtils.setField(post, "id", 1L);

        given(commentRepository.findAllOrderByParentIdAscNullsFirstByPostId(anyLong()))
                .willReturn(
                        List.of(createComment(null), createComment(null))
                );

        // when
        List<CommentDto> result = commentService.readAll(post.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

}