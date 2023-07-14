package dblab.sharing_flatform.domain.message;

import dblab.sharing_flatform.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;
    private String content;
    private boolean deleteBySender;
    private boolean deleteByReceiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiveMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member sendMember;

    public Message(String content, Member receiveMember, Member sendMember) {
        this.content = content;
        this.receiveMember = receiveMember;
        this.sendMember = sendMember;
        this.deleteBySender = false;
        this.deleteByReceiver = false;
    }

    public void deleteBySender() {
        this.deleteBySender = true;
    }
    public void deleteByReceiver() {
        this.deleteByReceiver = true;
    }

    public boolean isDeletable() {
        return isDeleteBySender() && isDeleteByReceiver();
    }

}
