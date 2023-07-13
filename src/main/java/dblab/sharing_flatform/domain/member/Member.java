package dblab.sharing_flatform.domain.member;

import dblab.sharing_flatform.domain.address.Address;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.OAuthMemberUpdateRequestDto;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    // roles -> 기본전략 : 지연로딩
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberRole> roles = new ArrayList<>();

    // post
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    public Member(String username, String password, String phoneNumber, Address address, String provider,  List<Role> roles, List<Post> posts) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.provider = provider;
        initPosts(posts);
        addRoles(roles);
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

    public void updateUserInfo(String password, String phoneNumber, Address address) {
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void updateMember(MemberUpdateRequestDto memberUpdateRequestDto){
        this.password = memberUpdateRequestDto.getPassword();
        this.phoneNumber = memberUpdateRequestDto.getPhoneNumber();
        this.address = memberUpdateRequestDto.getAddress();
    }

    public void updateOAuth2Member(OAuthMemberUpdateRequestDto requestDto){
        this.phoneNumber = requestDto.getPhoneNumber();
        this.address = requestDto.getAddress();
    }

}
