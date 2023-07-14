package dblab.sharing_flatform.domain.post;

import dblab.sharing_flatform.domain.base.BaseTime;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.image.PostImage;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.item.Item;
import dblab.sharing_flatform.dto.item.crud.update.ItemUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostImage> postImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    public Post(String title, String content, Category category, Item item, List<PostImage> postImages, Member member) {
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.category = category;
        this.item = item;
        this.member = member;
        this.postImages = new ArrayList<>();
        addImages(postImages);
    }

    public PostUpdateResponseDto updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        Map<String, List<PostImage>> m = new HashMap<>();

        this.title = postUpdateRequestDto.getTitle();
        this.content = postUpdateRequestDto.getContent();

        if (postUpdateRequestDto.getItemUpdateRequestDto() != null) {
            this.item = ItemUpdateRequestDto.toEntity(postUpdateRequestDto.getItemUpdateRequestDto());
        }

        // 수정/DB - 추가된 이미지 데이터베이스에 올리고, 삭제된 이미지 데이터베이스에서 삭제
        if (postUpdateRequestDto.getAddImages() != null) {
            List<MultipartFile> addImages = postUpdateRequestDto.getAddImages();  // 업로드할 이미지 파일
            List<PostImage> addList = addToDB(addImages);
            m.put("addList", addList);
        }

        if (postUpdateRequestDto.getDeleteImageNames() != null) {
            List<String> deleteImageNames = postUpdateRequestDto.getDeleteImageNames(); // 삭제할 이미지 파일 이름
            List<PostImage> deleteList = deleteFromDB(deleteImageNames);
            m.put("deleteList", deleteList);
        }

        return PostUpdateResponseDto.toDto(postUpdateRequestDto, this, m);
    }

    public List<PostImage> addToDB(List<MultipartFile> addImages) {
        List<PostImage> addPostImageList = MultipartToImage(addImages);
        addImages(addPostImageList);

        return addPostImageList;
    }

    public List<PostImage> deleteFromDB(List<String> deleteImageNames) {
        List<PostImage> deletePostImageList = StringToImage(deleteImageNames);
        deleteImages(deletePostImageList);

        return deletePostImageList;
    }

    private void addImages(List<PostImage> addList) {
        addList.stream().forEach(
                i -> {
                    postImages.add(i);
                    i.initPost(this);
                });
    }

    private void deleteImages(List<PostImage> deleteList) {
        deleteList.stream().forEach(
                i -> {
                    postImages.remove(i);
                    i.cancel(this);
                }
        );
    }

    public void initMember(Member member) {
        if (this.member == null) {
            this.member = member;
        }
    }

    private List<PostImage> StringToImage(List<String> deleteImageNames) {
        return deleteImageNames.stream().map(name -> convertNameToImage(name))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(Collectors.toList());
    }

    private Optional<PostImage> convertNameToImage(String name) {
        return this.postImages.stream().filter(i -> i.getOriginName().equals(name)).findAny();
    }

    private List<PostImage> MultipartToImage(List<MultipartFile> addImages) {
        return addImages.stream().map(
                file -> new PostImage(file.getOriginalFilename())).collect(Collectors.toList());
    }

}
