package dblab.sharing_platform.service.comment;

import dblab.sharing_platform.domain.comment.Comment;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.dto.comment.CommentCreateRequestDto;
import dblab.sharing_platform.dto.comment.CommentDto;
import dblab.sharing_platform.repository.comment.CommentRepository;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.post.PostRepository;
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

import static dblab.sharing_platform.factory.comment.CommentFactory.createComment;
import static dblab.sharing_platform.factory.member.MemberFactory.createMember;
import static dblab.sharing_platform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
        commentService.createCommentWithPostId(post.getId(), new CommentCreateRequestDto("content", null), member.getUsername());

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
        List<CommentDto> result = commentService.readAllCommentByPostId(post.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

}