package dblab.sharing_flatform.domain.member;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.domain.image.ProfileImage;
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

    @Nullable
    private String nickname;
    @Embedded
    @Nullable
    private Address address;
    private String provider;
    private String introduce;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberRole> roles = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    public Member(String username, String password, String nickname, String phoneNumber, Address address, String provider, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.provider = provider;
        this.profileImage = null;
        addRoles(roles);
    }

    private void addRoles(List<Role> roles) {
        List<MemberRole> roleList = roles.stream().map(role -> new MemberRole(this, role)).collect(Collectors.toList());
        this.roles = roleList;
    }

    public String updateMember(MemberUpdateRequestDto memberUpdateRequestDto, String encodedPassword){
        this.password = encodedPassword;
        this.nickname = memberUpdateRequestDto.getNickname();
        this.phoneNumber = memberUpdateRequestDto.getPhoneNumber();
        this.address = memberUpdateRequestDto.getAddress();
        this.introduce = memberUpdateRequestDto.getIntroduce();

        String existedImageName = updateProfileImage(memberUpdateRequestDto.getImage());

        return existedImageName;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public String updateOAuthMember(OAuthMemberUpdateRequestDto requestDto){
        this.phoneNumber = requestDto.getPhoneNumber();
        this.nickname = requestDto.getNickname();
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
