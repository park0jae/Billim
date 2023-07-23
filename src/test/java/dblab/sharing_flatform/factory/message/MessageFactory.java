package dblab.sharing_flatform.factory.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;

import static dblab.sharing_flatform.factory.member.MemberFactory.createReceiveMember;
import static dblab.sharing_flatform.factory.member.MemberFactory.createSendMember;

public class MessageFactory {

    public static Message createMessage() {
        return new Message("content", createReceiveMember(), createSendMember());
    }

    public static Message createMessageWithMeber(Member receiverMember, Member sendMember) {
        return new Message("content", receiverMember, sendMember);
    }
}
