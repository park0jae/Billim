package dblab.sharing_flatform.domain.member;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.domain.image.ProfileImage;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.OAuthMemberUpdateRequestDto;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String username;

    private String password;

    private String phoneNumber;

    @Embedded
    @Nullable
    private Address address;

    private String provider;

    private String introduce;

    private double rating;

    // roles -> 기본전략 : 지연로딩
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberRole> roles = new ArrayList<>();

    // post
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Review> reviews;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    public Member(String username, String password, String phoneNumber, Address address, String provider,  List<Role> roles, List<Post> posts) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.provider = provider;
        this.reviews = new ArrayList<>();
        this.rating = 0;
        this.profileImage = null;

        initPosts(posts);
        addRoles(roles);
    }

    public void calculateTotalStarRating(double rating){
        this.rating = Math.round(((this.rating + rating) / (double) reviews.size()) * 10) / 10.0;

    }

    public void addReviews(Review review){
        reviews.add(review);
        review.addMember(this);
    }

    private void addRoles(List<Role> roles) {
        List<MemberRole> roleList = roles.stream().map(role -> new MemberRole(this, role)).collect(Collectors.toList());
        this.roles = roleList;
    }

    private void initPosts(List<Post> posts) {
        if (!posts.isEmpty()) {
            posts.stream().forEach(
                p -> {
                    posts.add(p);
                    p.initMember(this);
                }
            );
        }
    }

    public String updateMember(MemberUpdateRequestDto memberUpdateRequestDto, String encodedPassword){
        this.password = encodedPassword;
        this.phoneNumber = memberUpdateRequestDto.getPhoneNumber();
        this.address = memberUpdateRequestDto.getAddress();
        this.introduce = memberUpdateRequestDto.getIntroduce();

        String existedImageName = updateProfileImage(memberUpdateRequestDto.getImage());

        return existedImageName;
    }

    public String updateOAuthMember(OAuthMemberUpdateRequestDto requestDto){
        this.phoneNumber = requestDto.getPhoneNumber();
        this.address = requestDto.getAddress();
        this.introduce = requestDto.getIntroduce();

        String existedImageName = updateProfileImage(requestDto.getImage());

        return existedImageName;
    }

    private String updateProfileImage(MultipartFile image) {
        String existedImageName = null;

        if (this.profileImage != null) {
            existedImageName = this.profileImage.getUniqueName();
        }
        if (image != null) {
            this.profileImage = new ProfileImage(image.getOriginalFilename());
        } else {
            this.profileImage = null;
        }
        return existedImageName;
    }

}
