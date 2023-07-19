package dblab.sharing_flatform.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.member.MemberRole;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.dto.image.profileImage.ProfileImageDto;
import dblab.sharing_flatform.dto.post.PostDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileDto {

    private ProfileImageDto profileImage;
    private String username;
    private String introduce;
    private RoleType memberRole;
    private double rating;
    private List<PostDto> posts;

    public static MemberProfileDto toDto(Member member, List<PostDto> postDtoList) {
        return new MemberProfileDto(
                ProfileImageDto.toDto(member.getProfileImage()),
                member.getUsername(),
                member.getIntroduce(),
                member.getRoles().get(0).getRole().getRoleType(),
                member.getRating(),
                postDtoList);
    }
}