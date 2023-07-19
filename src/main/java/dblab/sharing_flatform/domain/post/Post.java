package dblab.sharing_flatform.domain.post;

import dblab.sharing_flatform.domain.base.BaseTime;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.image.PostImage;
import dblab.sharing_flatform.domain.likepost.LikePost;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.embedded.item.Item;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.item.crud.update.ItemUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateResponseDto;
import dblab.sharing_flatform.helper.PostImageHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static dblab.sharing_flatform.helper.PostImageHelper.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @Embedded
    @Nullable
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Trade trade;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages;

    public Post(String title, String content, Category category, Item item, List<PostImage> postImages, Member member ,Trade trade) {
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.category = category;
        this.item = item;
        this.member = member;
        this.trade = trade;
        this.postImages = new ArrayList<>();

        addImages(postImages, this.postImages, this);
    }

    public PostUpdateResponseDto updatePost(PostUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();

        if (requestDto.getItemUpdateRequestDto() != null) {
            this.item = ItemUpdateRequestDto.toEntity(requestDto.getItemUpdateRequestDto());
        }

        Map<String, List<PostImage>> m = updateImage(requestDto, this.postImages, this);

        return PostUpdateResponseDto.toDto(requestDto, this, m);
    }

    public void likeUp() {
        this.likes ++;
    }

    public void likeDown() {
        this.likes --;
    }

    public void addTrade(Trade trade){
        if (this.trade == null) {
            this.trade = trade;
        }
    }
}
