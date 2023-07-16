package dblab.sharing_flatform.domain.trade;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "render_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member renderMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member borrowerMember;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @OneToOne(mappedBy = "trade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Review review;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean tradeComplete;

    public Trade(Member renderMember, Member borrowerMember,  LocalDate startDate, LocalDate endDate, Post post) {
        this.renderMember = renderMember;
        this.borrowerMember = borrowerMember;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tradeComplete = true;
        addPost(post);
    }

    public void addPost(Post post){
        this.post = post;
        post.addTrade(this);

    }

    public void addReview(Review review) {
        if (this.review == null) {
            this.review = review;
        }

    }
}

