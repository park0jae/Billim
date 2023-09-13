package dblab.sharing_platform.factory.message;

import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.message.Message;

import static dblab.sharing_platform.factory.member.MemberFactory.createReceiveMember;
import static dblab.sharing_platform.factory.member.MemberFactory.createSendMember;
import static dblab.sharing_platform.factory.post.PostFactory.createPost;

public class MessageFactory {

    public static Message createMessage() {
        return new Message("content", createReceiveMember(), createSendMember(), createPost());
    }

    public static Message createMessageWithMeber(Member receiverMember, Member sendMember) {
        return new Message("content", receiverMember, sendMember, createPost());
    }
}
