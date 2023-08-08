package dblab.sharing_platform.factory.comment;

import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.domain.comment.Comment;

import static dblab.sharing_platform.factory.member.MemberFactory.createMember;
import static dblab.sharing_platform.factory.post.PostFactory.createPost;

public class CommentFactory {

    public static Comment createComment(Post post, Member member, Comment parent) {
        if (parent == null) {
            return new Comment("content", true, post, member, null);
        } else {
            return new Comment("content", false, post, member, parent);
        }
    }

    public static Comment createComment(Comment parent) {
        return new Comment("content", parent == null ? false : true, createPost(), createMember(), parent);
    }
}
