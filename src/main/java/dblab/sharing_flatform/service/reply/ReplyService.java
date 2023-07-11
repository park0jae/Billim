package dblab.sharing_flatform.service.reply;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.reply.Reply;
import dblab.sharing_flatform.dto.reply.ReplyCreateRequestDto;
import dblab.sharing_flatform.dto.reply.ReplyDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.reply.NoRootCommentException;
import dblab.sharing_flatform.exception.reply.ReplyNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // create
    @Transactional
    public void create(ReplyCreateRequestDto req) {
        if (req.getParentCommentId() == null) {
            replyRepository.save(new Reply(req.getContent(),
                    postRepository.findById(req.getPostId()).orElseThrow(PostNotFoundException::new),
                    memberRepository.findByUsername(req.getUsername()).orElseThrow(MemberNotFoundException::new),
                    null
            ));
        } else if (req.getParentCommentId() != null) {
            Reply parent = replyRepository.findById(req.getParentCommentId()).orElseThrow(ReplyNotFoundException::new);
            if (parent.isRoot() == false) {
                throw new NoRootCommentException();
            }
            replyRepository.save(new Reply(req.getContent(),
                    postRepository.findById(req.getPostId()).orElseThrow(PostNotFoundException::new),
                    memberRepository.findByUsername(req.getUsername()).orElseThrow(MemberNotFoundException::new),
                    parent));
        }
    }

    // delete
    public void delete(Long id) {
        Reply reply = replyRepository.findById(id).orElseThrow(ReplyNotFoundException::new);
        reply.delete();
    }

    // read
    public List<ReplyDto> readAll(Long postId) {
        return ReplyDto.toDtoList(replyRepository.findAllByPostId(postId));
    }
}
