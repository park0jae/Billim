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

    private double starRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reviewerMember;

    @OneToOne
    @JoinColumn(name = "trade_id")
    private Trade trade;

    public Review (String content, double starRating, Member member, Member reviewerMember, Trade trade){
        this.content = content;
        this.starRating = starRating;
        this.member = member;
        this.reviewerMember = reviewerMember;
        this.trade = trade;
    }

    public void addMember(Member member) {
        this.member = member;
        member.calculateTotalStarRating(this.starRating);
    }
}
