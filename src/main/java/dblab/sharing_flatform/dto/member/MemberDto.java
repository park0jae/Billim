package dblab.sharing_flatform.dto.member;


import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long id;
    private String username;
    private RoleType memberRoles;

    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getUsername(), member.getRoles().get(0).getRole().getRoleType());
    }
}
