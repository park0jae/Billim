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
    public Long create(CommentCreateRequestDto req) {
        Post post = postRepository.findById(req.getPostId()).orElseThrow(PostNotFoundException::new);
        Member member = memberRepository.findByUsername(req.getUsername()).orElseThrow(MemberNotFoundException::new);

        Comment comment = commentRepository.save(new Comment(req.getContent(),
                req.getParentCommentId() == null ? true : false,
                post,
                member,
                req.getParentCommentId() == null ? null : commentRepository.findById(req.getParentCommentId()).orElseThrow(RootCommentNotFoundException::new)));

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
