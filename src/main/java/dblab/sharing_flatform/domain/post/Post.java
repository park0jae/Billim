package dblab.sharing_flatform.domain.post;

import dblab.sharing_flatform.domain.base.BaseTime;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.image.Image;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer likes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Item item;

    @OneToMany(mappedBy = "post",  cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Image> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    public Post(String title, String content, Category category, Item item, List<Image> images, Member member) {
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.category = category;
        this.item = item;
        this.member = member;
        this.images = new ArrayList<>();
        addImages(images);
    }


    private void addImages(List<Image> images) {

        for (int i = 0; i < images.size(); i++) {
            this.images.add(images.get(i));
            images.get(i).initPost(this);
        }
    }

    public void initMember(Member member) {
        if (this.member == null) {
            this.member = member;
        }
    }

    public void update(String title, String content, Category category, Item item, List<Image> images) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.item = item;
        addImages(images);
    }

}
