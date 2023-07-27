package dblab.sharing_flatform.service.comment;

import dblab.sharing_flatform.domain.comment.Comment;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.comment.CommentCreateRequestDto;
import dblab.sharing_flatform.dto.comment.CommentDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.comment.RootCommentNotFoundException;
import dblab.sharing_flatform.exception.comment.CommentNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // create
    @Transactional
    public Long create(Long postId, CommentCreateRequestDto requestDto) {
        Comment comment = commentRepository.save(
                new Comment(requestDto.getContent(),
                requestDto.getParentCommentId() == null ? true : false,
                postRepository.findById(postId).orElseThrow(PostNotFoundException::new),
                memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(MemberNotFoundException::new),
                requestDto.getParentCommentId() == null ? null : commentRepository.findById(requestDto.getParentCommentId()).orElseThrow(RootCommentNotFoundException::new)));

        return comment.getId();
    }

    // delete
    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.delete();
    }

    // read
    public List<CommentDto> readAll(Long postId) {
        return CommentDto.toDtoList(commentRepository.findAllOrderByParentIdAscNullsFirstByPostId(postId));
    }
}
