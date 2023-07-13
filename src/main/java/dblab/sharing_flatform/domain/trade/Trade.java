package dblab.sharing_flatform.domain.trade;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
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

    @OneToOne(mappedBy = "trade",fetch = FetchType.LAZY)
    private Post post;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean cancelByBorrower;
    private boolean cancelByRender;
    private boolean tradeComplete;

    public Trade(Member renderMember, Member borrowerMember,  LocalDate startDate, LocalDate endDate) {
        this.renderMember = renderMember;
        this.borrowerMember = borrowerMember;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cancelByBorrower = false;
        this.cancelByRender = false;
        this.tradeComplete = true;
    }

    public void cancelByBorrower() {
        this.cancelByBorrower = true;
    }
    public void cancelByRender() {
        this.cancelByRender = true;
    }

    public boolean isCancelable() {
        return isCancelByBorrower() || isCancelByRender();
    }

    public void addPost(Post post){
        this.post = post;
    }

}

