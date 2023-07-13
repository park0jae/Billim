package dblab.sharing_flatform.factory.comment;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.comment.Comment;

public class CommentFactory {

    public static Comment createReply(Post post, Member member, Comment parent) {
        if (parent == null) {
            return new Comment("content", true, post, member, null);
        } else {
            return new Comment("content", false, post, member, parent);
        }
    }
}
