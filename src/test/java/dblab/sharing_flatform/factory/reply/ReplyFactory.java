package dblab.sharing_flatform.factory.reply;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.reply.Reply;

public class ReplyFactory {

    public static Reply createReply(Post post, Member member, Reply parent) {
        if (parent == null) {
            return new Reply("content", true, post, member, null);
        } else {
            return new Reply("content", false, post, member, parent);
        }
    }
}
