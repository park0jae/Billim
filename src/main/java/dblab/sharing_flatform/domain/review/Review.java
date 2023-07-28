package dblab.sharing_flatform.domain.review;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.trade.Trade;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reviewerMember;

    public Review (String content, Member member, Member reviewerMember){
        this.content = content;
        this.member = member;
        this.reviewerMember = reviewerMember;
    }

}
